package com.tomato_planet.backend.model.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jianping5
 * @createDate 2022/11/12 21:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "操作密码请求体")
public class PasswordOperateRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户邮箱")
    private String userEmail;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
}
