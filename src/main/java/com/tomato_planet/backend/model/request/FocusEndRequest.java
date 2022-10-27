package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结束专注请求体
 *
 * @author jianping5
 * @createDate 2022/10/27 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "结束专注请求体")
public class FocusEndRequest implements Serializable {

    private static final long serialVersionUID = -1472073622481725772L;

    /**
     * 待办项id
     */
    @ApiModelProperty("待办项id")
    private Long todoItemId;

    /**
     * 待办专注时长（分钟）
     */
    @ApiModelProperty("待办专注时长（分钟）")
    private Integer focusTime;
}
