package com.tomato_planet.backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专注数据复杂视图体
 *
 * @author jianping5
 * @createDate 2022/11/8 20:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "专注数据复杂视图体")
public class FocusDataComplexVO implements Serializable {

    private static final long serialVersionUID = 8086570402657835239L;

    /**
     * 待办项名称
     */
    @ApiModelProperty("待办项名称")
    private String todoItemName;

    /**
     * 专注小时数
     */
    @ApiModelProperty("专注小时数")
    private Long focusHours;

    /**
     * 专注分钟数
     */
    @ApiModelProperty("专注分钟数")
    private Long focusMinutes;
}
