package org.multiverse.campusauction.entity.vo;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private int code;       // 状态码，例如 200、400、500
    private String message; // 提示信息
    private T data;         // 数据

    public ApiResponse() {}

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功返回
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, "success", null);
    }

    public static <T> ApiResponse<T> ok(String message,T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 失败返回
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(500, message, null);
    }

}
