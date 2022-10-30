package com.tomato_planet.backend.controller;

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.tomato_planet.backend.common.BaseResponse;
import com.tomato_planet.backend.common.ResultUtils;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.*;
import com.tomato_planet.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户控制类
 *
 * @author jianping5
 * @createDate 2022/10/24 16:30
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/login")

    @ApiOperation(value = "jdk-HashMap-动态创建显示参数-无@RequestBody")
    @DynamicResponseParameters(name = "CreateOrderHashMapModel",properties = {
            @DynamicParameter(name = "",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
            @DynamicParameter(name = "name3",value = "订单编号-gson"),
            @DynamicParameter(name = "name1",value = "订单编号1-gson"),
    })
    public BaseResponse<User> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        int loginType = loginRequest.getLoginType();
        String userIdentification = loginRequest.getUserIdentification();
        String pwdOrVerifyCode = loginRequest.getPwdOrVerifyCode();
        if (StringUtils.isAnyBlank(userIdentification, pwdOrVerifyCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        if (loginType != 0 && loginType != 1) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(loginType, userIdentification, pwdOrVerifyCode, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        if (registerRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        String userEmail = registerRequest.getUserEmail();
        String userAccount = registerRequest.getUserAccount();
        String emailVerifyCode = registerRequest.getEmailVerifyCode();
        if (StringUtils.isAnyBlank(userAccount, userEmail, emailVerifyCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = userService.userRegister(userAccount, userEmail, emailVerifyCode, request);
        return ResultUtils.success(id);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @PostMapping("/sendVerifyCode")
    public BaseResponse<Boolean> sendVerifyCode(@RequestBody VerifyCodeSendRequest verifyCodeSendRequest, HttpServletRequest request) {
        if (verifyCodeSendRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        String userEmail = verifyCodeSendRequest.getUserEmail();
        if (StringUtils.isBlank(userEmail)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = userService.sendVerifyCode(userEmail, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/password/recover")
    public BaseResponse<Boolean> recoverPassword(@RequestBody PasswordRecoverRequest recoverPasswordRequest, HttpServletRequest request) {
        if (recoverPasswordRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        String userEmail = recoverPasswordRequest.getUserEmail();
        String verifyCode = recoverPasswordRequest.getVerifyCode();
        String newPassword = recoverPasswordRequest.getNewPassword();
        if (StringUtils.isAnyBlank(verifyCode, newPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = userService.recoverPassword(userEmail, verifyCode, newPassword, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/password/change")
    public BaseResponse<Boolean> changePassword(@RequestBody PasswordChangeRequest changePasswordRequest) {
        if (changePasswordRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        String newConfirmPassword = changePasswordRequest.getNewConfirmPassword();
        if (StringUtils.isAnyBlank(oldPassword, newPassword, newConfirmPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = userService.changePassword(oldPassword, newPassword, newConfirmPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/password/set")
    public BaseResponse<Boolean> setPassword(@RequestBody PasswordSetRequest setPasswordRequest) {
        if (setPasswordRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        String password = setPasswordRequest.getPassword();
        String confirmPassword = setPasswordRequest.getConfirmPassword();
        if (StringUtils.isAnyBlank(password, confirmPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = userService.setPassword(password, confirmPassword);
        return ResultUtils.success(result);
    }
}
