package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.model.entity.TopicLike;
import com.tomato_planet.backend.service.TopicLikeService;
import com.tomato_planet.backend.mapper.TopicLikeMapper;
import org.springframework.stereotype.Service;

/**
* @author jianping5
* @description 针对表【like(点赞表)】的数据库操作Service实现
* @createDate 2022-10-30 00:32:45
*/
@Service
public class TopicLikeServiceImpl extends ServiceImpl<TopicLikeMapper, TopicLike>
    implements TopicLikeService {

}




