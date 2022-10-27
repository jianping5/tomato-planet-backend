package com.tomato_planet.backend.controller;

import com.tomato_planet.backend.common.BaseResponse;
import com.tomato_planet.backend.common.ResultUtils;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.DeleteRequest;
import com.tomato_planet.backend.model.request.TodoItemAddRequest;
import com.tomato_planet.backend.model.request.TodoItemUpdateRequest;
import com.tomato_planet.backend.model.vo.TodoItemVO;
import com.tomato_planet.backend.service.TodoItemService;
import com.tomato_planet.backend.util.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 待办项控制器
 *
 * @author jianping5
 * @createDate 2022/10/27 11:47
 */
@RestController
@RequestMapping("/todo")
public class TodoItemController {

    @Resource
    private TodoItemService todoItemService;

    @PostMapping("/add")
    public BaseResponse<Long> addTodoItem(@RequestBody TodoItemAddRequest todoItemAddRequest) {
        if (todoItemAddRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = UserHolder.getUser();
        TodoItem todoItem = new TodoItem();
        BeanUtils.copyProperties(todoItemAddRequest, todoItem);
        Long todoItemId = todoItemService.addTodoItem(todoItem, loginUser);
        return ResultUtils.success(todoItemId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTodoItem(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = UserHolder.getUser();
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = todoItemService.deleteTodoItem(id, loginUser);
        if (!result) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "删除待办失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTodoItem(@RequestBody TodoItemUpdateRequest todoItemUpdateRequest) {
        if (todoItemUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = UserHolder.getUser();
        boolean result = todoItemService.updateTodoItem(todoItemUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "更新待办失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("list")
    public BaseResponse<List<TodoItemVO>> listTodoItems() {
        // 获取当前登录用户
        User loginUser = UserHolder.getUser();
        List<TodoItemVO> todoItemVOList = todoItemService.listTodoItems(loginUser);
        return ResultUtils.success(todoItemVOList);
    }

}
