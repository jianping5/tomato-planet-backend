package com.tomato_planet.backend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 主题视图体
 *
 * @author jianping5
 * @createDate 2022/10/29 22:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "主题视图体")
public class TopicVO implements Serializable {

    private static final long serialVersionUID = 7921579278716294398L;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatarUrl;

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
     * 图片url
     */
    @ApiModelProperty("图片url")
    private String imageUrl;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Long likeCount;

    /**
     * 收藏数
     */
    @ApiModelProperty("收藏数")
    private Long collectCount;

    /**
     * 评论数
     */
    @ApiModelProperty("评论数")
    private Long commentCount;

    /**
     * 主题发表时间
     */
    @ApiModelProperty("主题发表时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime topicCreateTime;

    /**
     * 主题修改时间
     */
    @ApiModelProperty("主题修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime topicUpdateTime;

}
