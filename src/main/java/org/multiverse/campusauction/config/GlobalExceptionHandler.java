package org.multiverse.campusauction.config;

import cn.dev33.satoken.exception.NotLoginException;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(ApiException.class)
    public ApiResponse<?> handleApiException(ApiException ex) {
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * Sa-Token 未登录
     */
    @ExceptionHandler(NotLoginException.class)
    public ApiResponse<?> handleNotLogin(NotLoginException e) {
        return ApiResponse.fail(555, "登录已失效，请重新登录");
    }

    /**
     * 兜底异常（防止 500 泄漏到前端）
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.fail(500, "系统异常，请联系管理员");
    }
}

