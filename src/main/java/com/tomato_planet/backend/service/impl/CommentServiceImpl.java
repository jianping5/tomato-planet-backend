package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.model.entity.Comment;
import com.tomato_planet.backend.service.CommentService;
import com.tomato_planet.backend.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author jianping5
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




