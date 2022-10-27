package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jianping5
 * @createDate 2022/10/26 23:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "修改密码请求体")
public class PasswordChangeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @ApiModelProperty(value = "新确认密码")
    private String newConfirmPassword;
}
