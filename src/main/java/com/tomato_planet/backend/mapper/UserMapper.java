package com.tomato_planet.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomato_planet.backend.model.entity.User;

/**
* @author jianping5
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2022-10-24 11:52:01
* @Entity com.tomato_planet.backend.model.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取被逻辑删除的用户
     */
    User getOneByLogicDeleted(Long id);
}




