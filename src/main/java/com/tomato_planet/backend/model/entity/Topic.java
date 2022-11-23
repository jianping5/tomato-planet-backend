package com.tomato_planet.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 主题表
 * @TableName topic
 */
@TableName(value ="topic")
@Data
public class Topic implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 主题标题
     */
    private String topicTitle;

    /**
     * 主题内容
     */
    private String topicContent;

    /**
     * 标签
     */
    private String tag;

    /**
     * 图片url
     */
    private String imageUrl;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 是否被当前用户点赞
     */
    @TableField(exist = false)
    private Boolean isLiked;

    /**
     * 是否被当前用户收藏
     */
    @TableField(exist = false)
    private Boolean isCollected;

    /**
     * 收藏数
     */
    private Long collectCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 地理位置
     */
    private String location;

    /**
     * 主题发表时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime topicCreateTime;

    /**
     * 主题修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime topicUpdateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}