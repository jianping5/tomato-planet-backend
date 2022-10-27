package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.mapper.TopicMapper;
import org.springframework.stereotype.Service;

/**
* @author jianping5
* @description 针对表【topic(主题表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService{

}




