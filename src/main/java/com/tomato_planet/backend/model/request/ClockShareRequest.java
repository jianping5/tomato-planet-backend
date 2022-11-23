package com.tomato_planet.backend.model.request;

import com.tomato_planet.backend.constant.ClockType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 打卡分享请求体
 *
 * @author jianping5
 * @createDate 2022/11/6 12:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "打卡分享请求体")
public class ClockShareRequest implements Serializable {

    private static final long serialVersionUID = 5072583533203120281L;

    /**
     * 打卡类型
     */
    private int clockTypeNum;

    /**
     * 打卡枚举类
     */
    private ClockType clockType;





}
