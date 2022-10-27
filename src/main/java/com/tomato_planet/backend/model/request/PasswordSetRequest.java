package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设置密码请求体
 *
 * @author jianping5
 * @createDate 2022/10/26 23:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设置密码请求体")
public class PasswordSetRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
}
