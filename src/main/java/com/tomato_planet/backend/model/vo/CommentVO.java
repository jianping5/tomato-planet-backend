package com.tomato_planet.backend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论视图体
 *
 * @author jianping5
 * @createDate 2022/11/5 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "评论视图体")
public class CommentVO implements Serializable {

    private static final long serialVersionUID = -6648047041498207676L;

    /**
     * 评论id
     */
    @ApiModelProperty("评论id")
    private Long id;

    /**
     * 评论发布用户id
     */
    @ApiModelProperty("评论发布用户id")
    private Long userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 头像url
     */
    @ApiModelProperty("头像url")
    private String avatarUrl;

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
     * 父评论id
     */
    @ApiModelProperty("父评论id")
    private Long parentCommentId;

    /**
     * 评论者地理位置
     */
    @ApiModelProperty("评论者地理位置")
    private String location;

    /**
     * 评论时间
     */
    @ApiModelProperty("评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

}
