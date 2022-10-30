package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.constant.TopicConstant;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.Collection;
import com.tomato_planet.backend.model.entity.TopicLike;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.TopicUpdateRequest;
import com.tomato_planet.backend.model.vo.TopicVO;
import com.tomato_planet.backend.service.CollectionService;
import com.tomato_planet.backend.service.TopicLikeService;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.mapper.TopicMapper;
import com.tomato_planet.backend.service.UserService;
import com.tomato_planet.backend.util.ImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tomato_planet.backend.constant.TopicConstant.TOPIC_TITLE_LONGEST_LENGTH;

/**
* @author jianping5
* @description 针对表【topic(主题表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService{

    @Resource
    private ImageUtils imageUtils;

    @Resource
    private UserService userService;

    @Resource
    private TopicLikeService likeService;

    @Resource
    private CollectionService collectionService;

    @Override
    public long addTopic(Topic topic, User loginUser, MultipartFile[] files) {
        // 请求参数是否为空
        if (topic == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }

        // 校验主题标题
        String topicTitle = topic.getTopicTitle();
        if (StringUtils.isBlank(topicTitle) || topicTitle.length() > TOPIC_TITLE_LONGEST_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题标题不符合要求");
        }

        // 校验主题内容
        String topicContent = topic.getTopicContent();
        if (StringUtils.isBlank(topicContent)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题内容不符合要求");
        }

        // 获取登录用户id
        final long userId = loginUser.getId();

        // 文件上传云存储服务
        Map<String, List<String>> imagesUrl = imageUtils.uploadImages(files);

        // 设置当前用户id到topic中
        topic.setUserId(userId);

        // 设置图片url到topic中
        List<String> imageUrlList = imagesUrl.get("imageUrl");
        Gson gson = new Gson();
        String imagesUrlJson = gson.toJson(imageUrlList);
        topic.setImageUrl(imagesUrlJson);

        // 保存主题到数据库中
        boolean result = this.save(topic);
        Long topicId = topic.getId();
        if (!result || topicId == null || topicId <= 0) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "主题发布失败");
        }
        return topicId;
    }

    @Override
    public boolean updateTopic(TopicUpdateRequest topicUpdateRequest, User loginUser, MultipartFile[] files) {
        Long topicId = topicUpdateRequest.getId();

        // 校验是否登录（可以用拦截器替代）
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_AUTH, "未登录");
        }
        final Long loginUserId = loginUser.getId();

        if (topicId == null || topicId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 校验主题标题
        String topicTitle = topicUpdateRequest.getTopicTitle();
        if (StringUtils.isBlank(topicTitle) || topicTitle.length() > TOPIC_TITLE_LONGEST_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题标题不符合要求");
        }

        // 校验主题内容
        String topicContent = topicUpdateRequest.getTopicContent();
        if (StringUtils.isBlank(topicContent)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题内容不符合要求");
        }

        // 校验对应主题是否存在
        Topic oldTopic = getTopicById(topicId);

        // 校验是否有权限
        Long id = oldTopic.getUserId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        if (!id.equals(loginUserId)) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }

        // 上传图片，并获取url
        Map<String, List<String>> imagesUrl = imageUtils.uploadImages(files);
        List<String> imageUrlList = imagesUrl.get("imageUrl");
        Gson gson = new Gson();
        String imageUrlJson = gson.toJson(imageUrlList);

        // 创建Topic对象，并赋值
        Topic updateTopic = new Topic();
        BeanUtils.copyProperties(topicUpdateRequest, updateTopic);
        updateTopic.setImageUrl(imageUrlJson);

        return this.updateById(updateTopic);
    }

    @Override
    public boolean deleteTopic(Long id, User loginUser) {
        // 判断是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_AUTH, "未登录");
        }

        // 查看对应主题是否存在
        Topic oldTopic = getTopicById(id);

        // 校验是否有权限
        Long topicUserId = oldTopic.getUserId();
        if (!topicUserId.equals(loginUser.getId())) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }

        return this.removeById(id);
    }

    @Override
    public List<TopicVO> listTopicByTag(String tag) {
        // 判空
        if (StringUtils.isBlank(tag)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        List<TopicVO> topicVOList = new ArrayList<>();
        List<Topic> topicList = this.list();
        if (CollectionUtils.isEmpty(topicList)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "暂无任何主题");
        }
        // 过滤（只需要包含该分类的主题）
        List<Topic> collect = topicList.stream().filter(topic -> {
            String tagStrJson = topic.getTag();
            if (tagStrJson == null) {
                return false;
            }
            Gson gson = new Gson();
            List<String> tagList = gson.fromJson(tagStrJson, List.class);
            for (String tagName : tagList) {
                if (!tagName.contains(tag)) {
                    continue;
                }
                // 为主题视图体注入属性值
                TopicVO topicVO = new TopicVO();
                BeanUtils.copyProperties(topic, topicVO);
                long id = topicVO.getUserId();
                User user = userService.getById(id);
                topicVO.setUserName(user.getUserName());
                topicVO.setAvatarUrl(user.getAvatarUrl());
                topicVOList.add(topicVO);
            }
            return true;
        }).collect(Collectors.toList());

        return topicVOList;
    }

    @Override
    public List<TopicVO> searchTopicByTags(String keyWords) {
        // 判空
        if (StringUtils.isBlank(keyWords)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "输入不能为空");
        }
        // 从标签中查找
        List<TopicVO> topicVOListFromTag = listTopicByTag(keyWords);

        // 从标题中查找
        List<TopicVO> topicVOListFromTitle = new ArrayList<>();
        List<Topic> topicList = this.list();
        if (CollectionUtils.isEmpty(topicList)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "暂无任何主题");
        }
        topicList.stream().filter(topic -> {
            String topicTitle = topic.getTopicTitle();
            if (StringUtils.isBlank(topicTitle)) {
                return false;
            }
            if (topicTitle.contains(keyWords)) {
                TopicVO topicVO = new TopicVO();
                BeanUtils.copyProperties(topic, topicVO);
                long id = topic.getUserId();
                User user = userService.getById(id);
                topicVO.setUserName(user.getUserName());
                topicVO.setAvatarUrl(user.getAvatarUrl());
                topicVOListFromTitle.add(topicVO);
            }
            return true;

        }).collect(Collectors.toList());
        // 去重
        boolean result1 = topicVOListFromTag.removeAll(topicVOListFromTitle);
        if (!result1) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "数组去重失败");
        }
        // 合并
        boolean result2 = topicVOListFromTag.addAll(topicVOListFromTitle);
        if (!result2) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "数组合并失败");
        }
        return topicVOListFromTag;
    }

    @Override
    public TopicVO viewTopicDetail(Long id) {
        // 查看对应主题是否存在
        Topic topic = getTopicById(id);
        if (topic == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题不存在");
        }

        // 将主题属性值注入到主题视图体中
        TopicVO topicVO = new TopicVO();
        BeanUtils.copyProperties(topic, topicVO);

        // 查看主题发布者是否存在，若存在则将其部分信息注入到主题视图体中
        Long userId = topic.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该用户不存在");
        }
        topicVO.setUserName(user.getUserName());
        topicVO.setAvatarUrl(user.getAvatarUrl());
        return topicVO;
    }

    @Override
    public boolean likeTopic(Long id, User loginUser) {
        Topic topic = getTopicById(id);
        // 获取当前用户对该主题的点赞记录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN, "未登录");
        }
        Long loginUserId = loginUser.getId();
        QueryWrapper<TopicLike> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("user_id", loginUserId);
        likeQueryWrapper.eq("topic_id", topic.getId());
        TopicLike like = likeService.getOne(likeQueryWrapper);
        // 之前未点赞且无记录
        if (like == null) {
            TopicLike newlike = new TopicLike();
            newlike.setUserId(loginUserId);
            newlike.setTopicId(id);
            newlike.setIsLike(1);
            boolean saveResult = likeService.save(newlike);
            if (!saveResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "点赞失败");
            }
            // 增加对应主题的点赞量到
            topic.setLikeCount(topic.getLikeCount() + 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "点赞失败");
            }
            return true;
        }

        // 更新条件（对应主题对应用户id）
        UpdateWrapper<TopicLike> likeUpdateWrapper = new UpdateWrapper<>();
        likeUpdateWrapper.eq("user_id", loginUserId);
        likeUpdateWrapper.eq("topic_id", topic.getId());

        // 之前未点赞（但有记录）
        if (like.getIsLike() == 0) {
            likeUpdateWrapper.set("is_like", 1);
            boolean updateResult1 = likeService.update(likeUpdateWrapper);
            if (!updateResult1) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "点赞失败");
            }
            // 增加对应主题的点赞量到
            topic.setLikeCount(topic.getLikeCount() + 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "点赞失败");
            }
            return true;
        }

        // 之前已点赞
        if (like.getIsLike() == 1) {
            likeUpdateWrapper.set("is_like", 0);
            boolean updateResult2 = likeService.update(likeUpdateWrapper);
            if (!updateResult2) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "取消点赞失败");
            }
            // 减少对应主题的点赞量到
            topic.setLikeCount(topic.getLikeCount() - 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "点赞失败");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean collectTopic(Long id, User loginUser) {
        Topic topic = getTopicById(id);
        // 获取当前用户对该主题的收藏记录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN, "未登录");
        }
        Long loginUserId = loginUser.getId();
        QueryWrapper<Collection> collectionQueryWrapper = new QueryWrapper<>();
        collectionQueryWrapper.eq("user_id", loginUserId);
        collectionQueryWrapper.eq("topic_id", topic.getId());
        Collection collection = collectionService.getOne(collectionQueryWrapper);
        // 之前未收藏且无记录
        if (collection == null) {
            Collection newCollection = new Collection();
            newCollection.setUserId(loginUserId);
            newCollection.setTopicId(id);
            newCollection.setIsCollect(1);
            boolean saveResult = collectionService.save(newCollection);
            if (!saveResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏失败");
            }
            // 增加对应主题的收藏量到
            topic.setCollectCount(topic.getCollectCount() + 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏失败");
            }
            return true;
        }

        // 更新条件（对应主题对应用户id）
        UpdateWrapper<Collection> collectionUpdateWrapper = new UpdateWrapper<>();
        collectionUpdateWrapper.eq("user_id", loginUserId);
        collectionUpdateWrapper.eq("topic_id", topic.getId());

        // 之前未点赞（但有记录）
        if (collection.getIsCollect() == 0) {
            collectionUpdateWrapper.set("is_collect", 1);
            boolean updateResult1 = collectionService.update(collectionUpdateWrapper);
            if (!updateResult1) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "收藏失败");
            }
            // 增加对应主题的点赞量到
            topic.setCollectCount(topic.getCollectCount() + 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏失败");
            }
            return true;
        }

        // 之前已点赞
        if (collection.getIsCollect() == 1) {
            collectionUpdateWrapper.set("is_like", 0);
            boolean updateResult2 = collectionService.update(collectionUpdateWrapper);
            if (!updateResult2) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "取消收藏失败");
            }
            // 减少对应主题的点赞量到
            topic.setCollectCount(topic.getCollectCount() - 1);
            boolean updateResult = this.updateById(topic);
            if (!updateResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "收藏失败");
            }
            return true;
        }
        return false;
    }

    /**
     * 根据id查询对应主题
     * @param id
     * @return
     */
    private Topic getTopicById(Long id) {
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Topic topic = this.getById(id);
        if (topic == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题不存在");
        }
        return topic;
    }


}




