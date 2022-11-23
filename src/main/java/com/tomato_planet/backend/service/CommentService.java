package com.tomato_planet.backend.service;

import com.tomato_planet.backend.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.vo.CommentVO;

import java.util.List;

/**
* @author jianping5
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2022-10-24 11:52:01
*/
public interface CommentService extends IService<Comment> {

    /**
     * 评论主题
     * @param comment
     * @return
     */
    boolean commentTopic(Comment comment);

    /**
     * 查询主题评论
     * @param topicId
     * @return
     */
    List<CommentVO> listTopicComment(Long topicId);
}
