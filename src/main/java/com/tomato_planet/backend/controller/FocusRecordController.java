package com.tomato_planet.backend.controller;

import com.tomato_planet.backend.common.BaseResponse;
import com.tomato_planet.backend.common.ResultUtils;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.FocusRecord;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.FocusEndRequest;
import com.tomato_planet.backend.service.FocusRecordService;
import com.tomato_planet.backend.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 专注记录控制器
 *
 * @author jianping5
 * @createDate 2022/10/27 14:34
 */
@RestController
@RequestMapping("/focus")
public class FocusRecordController {

    @Resource
    private FocusRecordService focusRecordService;

    @PostMapping("/end")
    public BaseResponse<Long> endFocusState(@RequestBody FocusEndRequest focusEndRequest) {
        if (focusEndRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = UserHolder.getUser();
        FocusRecord focusRecord = new FocusRecord();
        BeanUtils.copyProperties(focusEndRequest, focusRecord);
        long focusRecordId = focusRecordService.endFocusState(focusRecord, loginUser);
        return ResultUtils.success(focusRecordId);
    }
}
