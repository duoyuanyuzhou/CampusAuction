package org.multiverse.campusauction.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.multiverse.campusauction.annotation.OwnerOrAdmin;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.service.UserService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class OwnerOrAdminAspect {

    private final UserService userService;

    public OwnerOrAdminAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(org.multiverse.campusauction.annotation.OwnerOrAdmin)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OwnerOrAdmin annotation = method.getAnnotation(OwnerOrAdmin.class);
        String resourceIdParam = annotation.resourceIdParam();

        // 获取方法参数名和值
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Object resourceId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(resourceIdParam)) {
                resourceId = args[i];
                break;
            }
        }

        if (resourceId == null) {
            resourceId = StpUtil.getLoginId();
        }


        if (resourceId == null) {
            return ApiResponse.fail("没有登陆，请登录");
        }

        Long loginId = Long.valueOf(resourceId.toString());

        if (!loginId.equals(Long.valueOf(resourceId.toString()))) {
            return ApiResponse.fail("不能访问");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, loginId)
                .eq(User::getStatus, 0)
                        .select(User::getId, User::getRole);
        User one = userService.getOne(wrapper);
        if (one == null) {
            return ApiResponse.fail("用户不存在");
        }

        if (one.getRole() != 2){
            return ApiResponse.fail("没有权限");
        }

        return joinPoint.proceed();
    }
}
