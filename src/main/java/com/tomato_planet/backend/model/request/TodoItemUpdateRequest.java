package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 修改待办项请求体
 *
 * @author jianping5
 * @createDate 2022/10/27 13:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "修改待办项请求体")
public class TodoItemUpdateRequest implements Serializable {

    private static final long serialVersionUID = -1836308229937123472L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

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
