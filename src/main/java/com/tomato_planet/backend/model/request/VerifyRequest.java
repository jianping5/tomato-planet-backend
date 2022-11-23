package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 邮箱验证请求体
 *
 * @author jianping5
 * @createDate 2022/11/12 21:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "邮箱验证请求体")
public class VerifyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户邮箱")
    private String userEmail;

    @ApiModelProperty(value = "邮箱验证码")
    private String emailVerifyCode;

}
