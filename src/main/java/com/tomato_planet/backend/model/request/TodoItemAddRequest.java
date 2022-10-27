package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加待办项请求体
 *
 * @author jianping5
 * @createDate 2022/10/27 12:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加待办项请求体")
public class TodoItemAddRequest implements Serializable {

    private static final long serialVersionUID = 2991107701285938798L;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 待办名称
     */
    @ApiModelProperty("待办名称")
    private String todoName;

    /**
     * 待办总时长（分钟）
     */
    @ApiModelProperty("待办总时长（分钟）")
    private Integer todoTotalTime;
}
