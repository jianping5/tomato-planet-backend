package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.FocusRecord;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.TodoItemUpdateRequest;
import com.tomato_planet.backend.model.vo.TodoItemVO;
import com.tomato_planet.backend.service.FocusRecordService;
import com.tomato_planet.backend.service.TodoItemService;
import com.tomato_planet.backend.mapper.TodoItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.tomato_planet.backend.constant.TodoItemConstant.*;

/**
* @author jianping5
* @description 针对表【todo_item(待办项表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class TodoItemServiceImpl extends ServiceImpl<TodoItemMapper, TodoItem>
    implements TodoItemService{

    @Resource
    private FocusRecordService focusRecordService;

    @Override
    public long addTodoItem(TodoItem todoItem, User loginUser) {
        // 请求参数是否为空
        if (todoItem == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // 校验待办名称长度 <= 30
        String todoName = todoItem.getTodoName();
        if (StringUtils.isNotBlank(todoName) && todoName.length() > TODO_NAME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办名称过长");
        }
        // 校验待办总时长（分钟） [1,180] 整数
        int todoTotalTime = todoItem.getTodoTotalTime();
        if (todoTotalTime <= TODO_TOTAL_SHORTEST_TIME_LENGTH && todoTotalTime > TODO_TOTAL_LOGNEST_TIME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不满足要求");
        }

        // TODO 背景图片

        // 插入待办项,并设置待办项对应用户id
        todoItem.setUserId(userId);
        boolean result = this.save(todoItem);
        Long todoItemId = todoItem.getId();
        if (!result || todoItemId == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "添加待办失败");
        }
        return todoItemId;

    }

    @Override
    public boolean deleteTodoItem(long id, User loginUser) {
        TodoItem todoItem = getTodoItemById(id);
        Long userId = todoItem.getUserId();
        // 校验是否有权限
        if (!userId.equals(loginUser.getId())) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }
        return this.removeById(id);
    }

    @Override
    public boolean updateTodoItem(TodoItemUpdateRequest todoItemUpdateRequest, User loginUser) {
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }
        final long loginUserId = loginUser.getId();
        // 校验待办名称长度 <= 30
        String todoName = todoItemUpdateRequest.getTodoName();
        if (StringUtils.isNotBlank(todoName) && todoName.length() > TODO_NAME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办名称过长");
        }
        // 校验待办总时长（分钟） [1,180] 整数
        int todoTotalTime = todoItemUpdateRequest.getTodoTotalTime();
        if (todoTotalTime <= TODO_TOTAL_SHORTEST_TIME_LENGTH && todoTotalTime >= TODO_TOTAL_LOGNEST_TIME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不满足要求");
        }

        // 校验旧的待办是否存在
        Long id = todoItemUpdateRequest.getId();
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TodoItem oldTodoItem = this.getById(id);
        if (oldTodoItem == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "待办不存在");
        }

        // 校验是否有权限
        long userId = oldTodoItem.getUserId();
        if (loginUserId != userId) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }

        TodoItem todoItem = new TodoItem();
        BeanUtils.copyProperties(todoItemUpdateRequest, todoItem);

        return this.updateById(todoItem);
    }

    @Override
    public List<TodoItemVO> listTodoItems(User loginUser) {
        // 获取当前登录用户的id，并查询与之相关的所有待办项
        long userId = loginUser.getId();
        QueryWrapper<TodoItem> todoItemQueryWrapper = new QueryWrapper<>();
        todoItemQueryWrapper.eq("user_id", userId);
        List<TodoItem> todoItemList = this.list(todoItemQueryWrapper);
        // 判空
        if (CollectionUtils.isEmpty(todoItemList)) {
            return new ArrayList<>();
        }
        List<TodoItemVO> todoItemVOList = new ArrayList<>();
        // 遍历所有待办项
        for (TodoItem todoItem : todoItemList) {
            TodoItemVO todoItemVO = new TodoItemVO();
            BeanUtils.copyProperties(todoItem, todoItemVO);
            // 查询该待办项对应的专注记录
            QueryWrapper<FocusRecord> focusRecordQueryWrapper = new QueryWrapper<>();
            focusRecordQueryWrapper.eq("user_id", userId).eq("todo_item_id", todoItem.getId());
            List<FocusRecord> focusRecordlist = focusRecordService.list(focusRecordQueryWrapper);
            // 专注记录为空，则该待办项专注时长为0
            if (CollectionUtils.isEmpty(focusRecordlist)) {
                todoItemVO.setFocusTotalTime(0);
                todoItemVOList.add(todoItemVO);
                continue;
            }
            // 遍历其所有专注记录，设置专注总时长
            int focusTotalTime = 0;
            for (FocusRecord focusRecord : focusRecordlist) {
                int focusTime = focusRecord.getFocusTime();
                focusTotalTime += focusTime;
            }
            todoItemVO.setFocusTotalTime(focusTotalTime);
            todoItemVOList.add(todoItemVO);
        }
        return todoItemVOList;
    }

    /**
     * 根据id查询对应待办
     * @param id
     * @return
     */
    private TodoItem getTodoItemById(Long id) {
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TodoItem todoItem = this.getById(id);
        if (todoItem == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该待办不存在");
        }
        return todoItem;
    }
}




