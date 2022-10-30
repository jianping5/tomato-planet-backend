package com.tomato_planet.backend.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主题查看DTO
 *
 * @author jianping5
 * @createDate 2022/10/30 22:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "主题DTO")
public class TopicDTO implements Serializable {

    private static final long serialVersionUID = 7596770279294828564L;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long id;
}
