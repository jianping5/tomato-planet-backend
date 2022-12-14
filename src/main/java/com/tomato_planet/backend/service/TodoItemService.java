package com.tomato_planet.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.TodoItemUpdateRequest;
import com.tomato_planet.backend.model.vo.TodoItemVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author jianping5
* @description 针对表【todo_item(待办项表)】的数据库操作Service
* @createDate 2022-11-08 23:54:02
*/
public interface TodoItemService extends IService<TodoItem> {

    /**
     * 添加待办项
     * @param todoItem
     * @param loginUser
     * @return
     */
    long addTodoItem(TodoItem todoItem, User loginUser);

    /**
     * 删除待办项
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTodoItem(long id, User loginUser);

    /**
     * 更新待办项
     * @param todoItemUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTodoItem(TodoItemUpdateRequest todoItemUpdateRequest, User loginUser);

    List<TodoItemVO> listTodoItems(User loginUser);

    long clockShare(User loginUser, MultipartFile file, Integer clockType);
}
