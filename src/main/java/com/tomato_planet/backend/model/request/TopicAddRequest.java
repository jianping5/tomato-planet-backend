package com.tomato_planet.backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加主题请求体
 *
 * @author jianping5
 * @createDate 2022/10/29 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加主题请求体")
public class TopicAddRequest implements Serializable {

    private static final long serialVersionUID = -7081552488751192718L;

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
