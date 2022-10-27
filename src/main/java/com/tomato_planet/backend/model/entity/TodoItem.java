package com.tomato_planet.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 待办项表
 * @TableName todo_item
 */
@TableName(value ="todo_item")
@Data
public class TodoItem implements Serializable {
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
     * 待办名称
     */
    private String todoName;

    /**
     * 待办总时长（分钟）
     */
    private Integer todoTotalTime;

    /**
     * 待办背景图片
     */
    private String todoBackgroundImageUrl;

    /**
     * 待办创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime todoCreateTime;

    /**
     * 待办修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime todoUpdateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TodoItem other = (TodoItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTodoName() == null ? other.getTodoName() == null : this.getTodoName().equals(other.getTodoName()))
            && (this.getTodoTotalTime() == null ? other.getTodoTotalTime() == null : this.getTodoTotalTime().equals(other.getTodoTotalTime()))
            && (this.getTodoBackgroundImageUrl() == null ? other.getTodoBackgroundImageUrl() == null : this.getTodoBackgroundImageUrl().equals(other.getTodoBackgroundImageUrl()))
            && (this.getTodoCreateTime() == null ? other.getTodoCreateTime() == null : this.getTodoCreateTime().equals(other.getTodoCreateTime()))
            && (this.getTodoUpdateTime() == null ? other.getTodoUpdateTime() == null : this.getTodoUpdateTime().equals(other.getTodoUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTodoName() == null) ? 0 : getTodoName().hashCode());
        result = prime * result + ((getTodoTotalTime() == null) ? 0 : getTodoTotalTime().hashCode());
        result = prime * result + ((getTodoBackgroundImageUrl() == null) ? 0 : getTodoBackgroundImageUrl().hashCode());
        result = prime * result + ((getTodoCreateTime() == null) ? 0 : getTodoCreateTime().hashCode());
        result = prime * result + ((getTodoUpdateTime() == null) ? 0 : getTodoUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", todoName=").append(todoName);
        sb.append(", todoTotalTime=").append(todoTotalTime);
        sb.append(", todoBackgroundImageUrl=").append(todoBackgroundImageUrl);
        sb.append(", todoCreateTime=").append(todoCreateTime);
        sb.append(", todoUpdateTime=").append(todoUpdateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}