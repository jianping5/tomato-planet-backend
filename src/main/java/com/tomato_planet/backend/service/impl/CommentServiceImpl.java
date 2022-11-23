package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.webkit.dom.CommentImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.Comment;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.vo.CommentVO;
import com.tomato_planet.backend.service.CommentService;
import com.tomato_planet.backend.mapper.CommentMapper;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.service.UserService;
import com.tomato_planet.backend.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author jianping5
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private TopicService topicService;

    @Resource
    private UserService userService;

    @Override
    public boolean commentTopic(Comment comment) {
        // 获取当前登录用户的id
        Long loginUserId = UserHolder.getUser().getId();

        // 设置用户id到评论中（暂不考虑父评论id），并插入评论表中
        if (comment == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        comment.setUserId(loginUserId);
        boolean saveResult = this.save(comment);
        if (!saveResult) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "评论失败");
        }

        // 将对应主题的评论量 + 1
        Long topicId = comment.getTopicId();
        if (topicId == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UpdateWrapper<Topic> topicUpdateWrapper = new UpdateWrapper<>();
        topicUpdateWrapper.setSql("comment_count = comment_count + 1").eq("id", topicId);
        boolean result = topicService.update(topicUpdateWrapper);

        return result;
    }

    @Override
    public List<CommentVO> listTopicComment(Long topicId) {
        // 判断对应主题是否存在
        Topic topic = topicService.getById(topicId);
        if (topic == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该主题不存在");
        }

        // 查出所有评论，并将评论信息和用户部分信息注入到评论视图体中
        List<CommentVO> commentVOList = new ArrayList<>();
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("topic_id", topicId);
        List<Comment> commentList = this.list(commentQueryWrapper);
        if (CollectionUtils.isEmpty(commentList)) {
            return new ArrayList<>();
        }
        commentList.forEach(comment -> {
            commentVOList.add(injectCommentVO(comment));
        });
        return commentVOList;
    }

    /**
     * 注入属性到评论视图体
     * @return
     */
    private CommentVO injectCommentVO(Comment comment) {
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        User user = userService.getById(comment.getUserId());
        commentVO.setUserName(user.getUserName());
        commentVO.setAvatarUrl(user.getAvatarUrl());
        return commentVO;
    }
}




