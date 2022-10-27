package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.FocusRecord;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.FocusEndRequest;
import com.tomato_planet.backend.service.FocusRecordService;
import com.tomato_planet.backend.mapper.FocusRecordMapper;
import com.tomato_planet.backend.service.TodoItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author jianping5
* @description 针对表【focus_record(专注记录表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class FocusRecordServiceImpl extends ServiceImpl<FocusRecordMapper, FocusRecord>
    implements FocusRecordService{

    @Resource
    private TodoItemService todoItemService;

    @Override
    public long endFocusState(FocusRecord focusRecord, User loginUser) {
        // 校验请求是否为空
        if (focusRecord == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取登录用户的id
        long loginUserId = loginUser.getId();

        // 校验对应待办项是否存在
        Long todoItemId = focusRecord.getTodoItemId();
        if (todoItemId == null || todoItemId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TodoItem todoItem = todoItemService.getById(todoItemId);
        if (todoItem == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "对应待办不存在");
        }

        // 校验专注时长
        Integer focusTime = focusRecord.getFocusTime();
        if (focusTime == null || focusTime < 1) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "专注时长过短");
        }

        // 插入专注记录表中（设置对应userId）
        focusRecord.setUserId(loginUserId);
        boolean result = this.save(focusRecord);
        Long focusRecordId = focusRecord.getId();
        if (!result || focusRecordId == null || focusRecordId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "添加专注记录失败");
        }

        return focusRecordId;
    }
}




