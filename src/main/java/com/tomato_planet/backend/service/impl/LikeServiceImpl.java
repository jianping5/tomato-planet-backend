package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.model.entity.Like;
import com.tomato_planet.backend.service.LikeService;
import com.tomato_planet.backend.mapper.LikeMapper;
import org.springframework.stereotype.Service;

/**
* @author jianping5
* @description 针对表【like(点赞表)】的数据库操作Service实现
* @createDate 2022-10-30 00:32:45
*/
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like>
    implements LikeService{

}




