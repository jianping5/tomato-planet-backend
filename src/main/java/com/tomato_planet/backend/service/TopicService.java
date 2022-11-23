package com.tomato_planet.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.TopicUpdateRequest;
import com.tomato_planet.backend.model.vo.TopicVO;
import jdk.nashorn.internal.ir.LiteralNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author jianping5
* @description 针对表【topic(主题表)】的数据库操作Service
* @createDate 2022-10-24 11:52:01
*/
public interface TopicService extends IService<Topic> {


    /**
     * 发布主题
     * @param topic
     * @param loginUser
     * @param files
     * @return
     */
    long addTopic(Topic topic, User loginUser, MultipartFile[] files);

    /**
     * 更新主题
     * @param topicUpdateRequest
     * @param loginUser
     * @param files
     * @return
     */
    boolean updateTopic(TopicUpdateRequest topicUpdateRequest, User loginUser, MultipartFile[] files);

    /**
     * 删除主题
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTopic(Long id, User loginUser);

    /**
     * 根据标签分类展示主题
     * @param tag
     * @return
     */
    List<TopicVO> listTopicByTag(String tag);

    /**
     * 根据关键字查询主题（从标签和标题中查询）
     * @param keyWords
     */
    List<TopicVO> searchTopic(String keyWords);

    /**
     * 查看主题详情
     * @param id
     * @return
     */
    TopicVO viewTopicDetail(Long id);

    /**
     * 点赞主题
     * @param id
     * @param loginUser
     * @return
     */
    boolean likeTopic(Long id, User loginUser);

    /**
     * 收藏主题
     * @param id
     * @param loginUser
     * @return
     */
    boolean collectTopic(Long id, User loginUser);

    /**
     * 点赞主题（redis）
     * @param topicId
     * @return
     */
    boolean likeTopic(Long topicId);


    /**
     * 查看主题收藏记录
     * @return
     */
    List<TopicVO> listCollectedTopic();

    /**
     * 查找发布管理记录
     * @return
     */
    List<TopicVO> listPublishTopic();
}
