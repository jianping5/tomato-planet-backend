package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jianping5
 * @createDate 2022/10/26 20:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录请求体")
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录类型（0：邮箱登录 1：密码登录）", example = "1")
    private int loginType;

    @ApiModelProperty(value = "用户身份（账号或邮箱）", example = "01234567899")
    private String userIdentification;

    @ApiModelProperty(value = "账号或邮箱", example = "wjp0123456789")
    private String pwdOrVerifyCode;
}
