package com.tomato_planet.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author jianping5
 * @createDate 2022/10/24 12:10
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(StatusCode statusCode) {
        this(statusCode.getCode(), null, statusCode.getMessage(), statusCode.getDescription());
    }

}
