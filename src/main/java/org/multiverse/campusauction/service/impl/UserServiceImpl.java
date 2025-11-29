package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.User;
import org.multiverse.campusauction.service.UserService;
import org.multiverse.campusauction.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




