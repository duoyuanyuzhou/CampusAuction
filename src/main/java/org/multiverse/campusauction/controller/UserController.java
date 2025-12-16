package org.multiverse.campusauction.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.multiverse.campusauction.annotation.CheckPermission;
import org.multiverse.campusauction.annotation.OwnerOrAdmin;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.entity.vo.UserQuery;
import org.multiverse.campusauction.service.UserService;
import org.multiverse.campusauction.util.Argon2Util;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse registerUser(@RequestBody User user) {
        ApiResponse register = userService.register(user);
        return register;
    }

    @PostMapping("/login")
    public ApiResponse<User> loginUser(@RequestBody User user) {
        ApiResponse login = userService.login(user);
        if (login.getData() != null) {
            User loginUser = (User)login.getData();
            StpUtil.login(loginUser.getId());
        }
        return login;
    }

    @CheckPermission
    @GetMapping("/getUser")
    public ApiResponse<User> getUser(@RequestParam("id") String id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id);
        User user = userService.getOne(wrapper);
        return ApiResponse.ok(user);
    }

    @CheckPermission
    @GetMapping("/getUserList")
    public ApiResponse<IPage<User>> getUserList(@ParameterObject Page<User> page, @ParameterObject UserQuery userQuery) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(userQuery.getUsername() != null && !userQuery.getUsername().isEmpty(),
                User::getUsername,
                userQuery.getUsername());

        Page<User> userPage = userService.page(page, wrapper);
        return ApiResponse.ok(userPage);
    }

    @PostMapping
    @CheckPermission("admin") // 需要权限注解，可选
    public ApiResponse deleteUser(@RequestParam("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            return ApiResponse.fail("用户ID不能为空");
        }

        Long userId;
        try {
            userId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ApiResponse.fail("用户ID格式不正确");
        }

        boolean removed = userService.removeById(userId); // MyBatis-Plus 提供的删除方法
        if (!removed) {
            return ApiResponse.fail("删除失败，用户可能不存在");
        }

        return ApiResponse.ok("删除成功");
    }

    @OwnerOrAdmin
    @PutMapping("/updateUser")
    public ApiResponse updateUser(@RequestBody User user) {
        if (user == null) {
            return ApiResponse.fail("数据为空");
        }


        LambdaQueryWrapper<User> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(User::getUsername, user.getUsername())
                .ne(User::getId, user.getId()); // 排除当前用户
        User existUserByUsername = userService.getOne(usernameWrapper);
        if (existUserByUsername != null) {
            return ApiResponse.fail("用户名已存在");
        }


        LambdaQueryWrapper<User> nicknameWrapper = new LambdaQueryWrapper<>();
        nicknameWrapper.eq(User::getNickname, user.getNickname())
                .ne(User::getId, user.getId());
        User existUserByNickname = userService.getOne(nicknameWrapper);
        if (existUserByNickname != null) {
            return ApiResponse.fail("昵称已存在");
        }

        if (user.getPassword() != null) {
            String hash = Argon2Util.hash(user.getPassword());
            user.setPassword(hash);
        }

        boolean b = userService.updateById(user);
        if (!b) {
            return ApiResponse.ok("更新失败");
        }
        return ApiResponse.ok("更新成功");

    }


}
