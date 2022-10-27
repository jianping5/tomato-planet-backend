package com.tomato_planet.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 专注记录表
 * @TableName focus_record
 */
@TableName(value ="focus_record")
@Data
public class FocusRecord implements Serializable {
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
     * 待办项id
     */
    private Long todoItemId;

    /**
     * 待办专注时长（分钟）
     */
    private Integer focusTime;

    /**
     * 待办专注结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime focusEndTime;

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
        FocusRecord other = (FocusRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTodoItemId() == null ? other.getTodoItemId() == null : this.getTodoItemId().equals(other.getTodoItemId()))
            && (this.getFocusTime() == null ? other.getFocusTime() == null : this.getFocusTime().equals(other.getFocusTime()))
            && (this.getFocusEndTime() == null ? other.getFocusEndTime() == null : this.getFocusEndTime().equals(other.getFocusEndTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTodoItemId() == null) ? 0 : getTodoItemId().hashCode());
        result = prime * result + ((getFocusTime() == null) ? 0 : getFocusTime().hashCode());
        result = prime * result + ((getFocusEndTime() == null) ? 0 : getFocusEndTime().hashCode());
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
        sb.append(", todoItemId=").append(todoItemId);
        sb.append(", focusTime=").append(focusTime);
        sb.append(", focusEndTime=").append(focusEndTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}