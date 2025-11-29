package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.UserMessage;
import org.multiverse.campusauction.service.UserMessageService;
import org.multiverse.campusauction.mapper.UserMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【user_message(用户消息表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService{

}




