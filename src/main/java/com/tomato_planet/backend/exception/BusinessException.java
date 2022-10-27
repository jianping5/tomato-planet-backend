package com.tomato_planet.backend.exception;

import com.tomato_planet.backend.common.StatusCode;

/**
 * 自定义异常类
 *
 * @author jianping5
 * @createDate 2022/10/24 16:06
 */
public class BusinessException extends RuntimeException{

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.description = statusCode.getDescription();
    }

    public BusinessException(StatusCode statusCode, String description) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
