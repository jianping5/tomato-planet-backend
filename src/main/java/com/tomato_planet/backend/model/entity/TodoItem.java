package com.tomato_planet.backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
    private Date todoCreateTime;

    /**
     * 待办修改时间
     */
    private Date todoUpdateTime;

    /**
     * 是否删除（逻辑删除）
     */
    @TableLogic
    private Integer isDeleted;

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
            && (this.getTodoUpdateTime() == null ? other.getTodoUpdateTime() == null : this.getTodoUpdateTime().equals(other.getTodoUpdateTime()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
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
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
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
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}