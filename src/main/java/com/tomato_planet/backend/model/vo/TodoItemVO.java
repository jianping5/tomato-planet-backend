package com.tomato_planet.backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 待办项视图体
 *
 * @author jianping5
 * @createDate 2022/10/27 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "待办项视图体")
public class TodoItemVO implements Serializable {

    private static final long serialVersionUID = -6648047041498207676L;

    /**
     * 待办项id
     */
    @ApiModelProperty("待办项id")
    private Long id;

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

    /**
     * 待办专注总时长（分钟）
     */
    @ApiModelProperty("待办专注总时长（分钟）")
    private Integer focusTotalTime;

    /**
     * 待办背景图片
     */
    @ApiModelProperty("待办背景图片")
    private String todoBackgroundImageUrl;

}
