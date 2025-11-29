package org.multiverse.campusauction.config;

import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e) {
        e.printStackTrace(); // 记录日志
        return ApiResponse.fail(500, "服务器内部错误：" + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.fail(400, e.getMessage());
    }
}
