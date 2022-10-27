package com.tomato_planet.backend.common;

/**
 * 返回工具类
 *
 * @author jianping5
 * @createDate 2022/10/24 15:42
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<>(0, data, "ok", description);
    }

    /**
     * 失败
     * @param statusCode
     * @return
     */
    public static BaseResponse error(StatusCode statusCode) {
        return new BaseResponse(statusCode);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    /**
     * 失败
     * @param statusCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(StatusCode statusCode, String message, String description) {
        return new BaseResponse(statusCode.getCode(), message, description);
    }

    /**
     * 失败
     * @param statusCode
     * @param description
     * @return
     */
    public static BaseResponse error(StatusCode statusCode, String description) {
        return new BaseResponse(statusCode.getCode(), description);
    }
}
