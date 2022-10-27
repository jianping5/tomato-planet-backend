package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码发送请求体
 *
 * @author jianping5
 * @createDate 2022/10/27 19:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("验证码发送请求体")
public class VerifyCodeSendRequest implements Serializable {

    private static final long serialVersionUID = 954228626886790196L;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String userEmail;

}
