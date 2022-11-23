package com.tomato_planet.backend.model.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主题评论请求
 *
 * @author jianping5
 * @createDate 2022/11/5 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("主题评论请求")
public class TopicCommentRequest implements Serializable {

    private static final long serialVersionUID = 5072583533203120281L;

    /**
     * 评论主题id
     */
    @ApiModelProperty("评论主题id")
    private Long topicId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String commentContent;

    /**
     * 评论者地理位置
     */
    @ApiModelProperty("评论者地理位置")
    private String location;

}
