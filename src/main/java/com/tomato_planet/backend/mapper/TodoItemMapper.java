package com.tomato_planet.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tomato_planet.backend.model.entity.TodoItem;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @author jianping5
* @description 针对表【todo_item(待办项表)】的数据库操作Mapper
* @createDate 2022-10-24 11:52:01
* @Entity com.tomato_planet.backend.model.entity.TodoItem
*/
public interface TodoItemMapper extends BaseMapper<TodoItem> {

    List<TodoItem> listAllByIds(@Param("idList") Collection<? extends Serializable> idList);
}




