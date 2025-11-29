package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.PaymentRecord;
import org.multiverse.campusauction.service.PaymentRecordService;
import org.multiverse.campusauction.mapper.PaymentRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【payment_record(支付记录表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord>
    implements PaymentRecordService{

}




