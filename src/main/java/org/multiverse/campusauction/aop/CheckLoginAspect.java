package org.multiverse.campusauction.aop;


import cn.dev33.satoken.stp.StpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.multiverse.campusauction.annotation.CheckPermission;
import org.multiverse.campusauction.exception.ApiException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckLoginAspect {

    @Around("@annotation(checkPermission)")
    public Object doPermissionCheck(ProceedingJoinPoint pjp,
                                    CheckPermission checkPermission) throws Throwable {

        // 是否登录（Sa-Token 官方推荐）
        if (!StpUtil.isLogin()) {
            throw new ApiException(401, "登录已失效，请重新登录");
        }

        // 如果你后面要做权限判断，可以在这里加
        // String role = checkPermission.role();

        return pjp.proceed();
    }
}

