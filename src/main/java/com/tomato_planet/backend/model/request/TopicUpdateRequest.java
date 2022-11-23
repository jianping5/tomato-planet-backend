package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主题更新请求体
 *
 * @author jianping5
 * @createDate 2022/10/29 20:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("主题更新请求体")
public class TopicUpdateRequest implements Serializable {

    private static final long serialVersionUID = -3916258767756348871L;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long id;

    /**
     * 主题标题
     */
    @ApiModelProperty("主题标题")
    private String topicTitle;

    /**
     * 主题内容
     */
    @ApiModelProperty("主题内容")
    private String topicContent;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String tag;

    /**
     * 地理位置
     */
    @ApiModelProperty("地理位置")
    private String location;

}
