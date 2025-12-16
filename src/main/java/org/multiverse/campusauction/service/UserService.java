package org.multiverse.campusauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.ApiResponse;

/**
* @author jiaxi
* @description 针对表【user】的数据库操作Service
* @createDate 2025-11-29 17:28:16
*/
public interface UserService extends IService<User> {

    ApiResponse<User> register(User user);

    ApiResponse<User> login(User user);
}
