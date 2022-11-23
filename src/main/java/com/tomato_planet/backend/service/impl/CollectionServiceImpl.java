package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.model.entity.Collection;
import com.tomato_planet.backend.service.CollectionService;
import com.tomato_planet.backend.mapper.CollectionMapper;
import org.springframework.stereotype.Service;


/**
* @author jianping5
* @description 针对表【collection(收藏表)】的数据库操作Service实现
* @createDate 2022-10-31 00:01:34
*/
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection>
    implements CollectionService{

}




