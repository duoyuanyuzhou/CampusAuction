package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.service.UserService;
import org.multiverse.campusauction.mapper.UserMapper;
import org.multiverse.campusauction.util.Argon2Util;
import org.multiverse.campusauction.util.UserUtil;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public ApiResponse register(User user) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ApiResponse.fail("用户名不能为空");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ApiResponse.fail("密码不能为空");
        }

        if (user.getPassword().length() < 6) {
            return ApiResponse.fail("密码不能少于 6 位");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        long count = this.count(wrapper);
        if (count > 0) {
            return ApiResponse.fail("用户名已存在");
        }

        user.setPassword(Argon2Util.hash(user.getPassword()));


        boolean saved = this.save(user);

        if (!saved) {
            return ApiResponse.fail("注册失败，请稍后再试");
        }

        User safeUser = UserUtil.getSafeUser(user);
        return ApiResponse.ok("注册成功", safeUser);
    }

    @Override
    public ApiResponse<User> login(User user) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ApiResponse.fail("账户或密码为空");
        }


        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());

        User dbUser = this.getOne(wrapper);

        if (dbUser == null) {
            return ApiResponse.fail("账号不存在");
        }

        boolean match = Argon2Util.verify(dbUser.getPassword(), user.getPassword());
        if (!match) {
            return ApiResponse.fail("密码错误");
        }
        User safeUser = UserUtil.getSafeUser(dbUser);
        return ApiResponse.ok("登录成功", safeUser);
    }

}




