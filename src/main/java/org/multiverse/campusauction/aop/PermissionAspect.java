package org.multiverse.campusauction.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.multiverse.campusauction.annotation.CheckPermission;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.exception.ApiException;
import org.multiverse.campusauction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private UserService  userService;

    @Around("@annotation(checkPermission)")
    public Object  doPermissionCheck(ProceedingJoinPoint pjp, CheckPermission checkPermission) throws Throwable {
        Object loginIdObj = null;
        try {
            loginIdObj = StpUtil.getLoginId();
        }catch (Exception e){
            throw new ApiException(555,"token过期");
        }
        if (loginIdObj == null) {
            return ApiResponse.fail("没有登陆，请登录");
        }

        Long loginId = Long.valueOf(loginIdObj.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, loginId)
                .select(User::getId, User::getRole, User::getStatus);

        User one = userService.getOne(wrapper);
        if (one == null) {
            return ApiResponse.fail("没有账户");
        }

        if (one.getStatus() == 1) {
            return ApiResponse.fail("账户被封禁");
        }

        if (one.getRole() != 2) {
            return ApiResponse.fail("没有权限");
        }


        return pjp.proceed();
    }
}
