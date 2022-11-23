package com.tomato_planet.backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专注数据简单视图体
 *
 * @author jianping5
 * @createDate 2022/11/8 19:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "专注数据简单视图体")
public class FocusDataSimpleVO implements Serializable {

    private static final long serialVersionUID = 8086570402657835239L;

    /**
     * 专注次数
     */
    @ApiModelProperty("专注次数")
    private Long focusCount;

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

    /**
     * 专注平均小时数
     */
    @ApiModelProperty("专注平均小时数")
    private Long focusAveHours;

    /**
     * 专注平均分钟数
     */
    @ApiModelProperty("专注平均分钟数")
    private Long focusAveMinutes;

    /**
     * 当日专注次数
     */
    @ApiModelProperty("当日专注次数")
    private Integer focusDayCount;

    /**
     * 当日专注小时数
     */
    @ApiModelProperty("当日专注小时数")
    private Integer focusDayHours;

    /**
     * 当日专注分钟数
     */
    @ApiModelProperty("当日专注分钟数")
    private Integer focusDayMinutes;

}
