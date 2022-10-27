package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 删除请求体
 *
 * @author jianping5
 * @createDate 2022/10/27 13:24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "删除请求体")
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 7520428427245010808L;

    /**
     * 删除个体的id
     */
    @ApiModelProperty("删除个体的id")
    private Long id;

}
