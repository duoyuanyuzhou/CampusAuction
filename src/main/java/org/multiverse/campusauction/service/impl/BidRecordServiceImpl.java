package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.service.BidRecordService;
import org.multiverse.campusauction.mapper.BidRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【bid_record(竞价记录表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class BidRecordServiceImpl extends ServiceImpl<BidRecordMapper, BidRecord>
    implements BidRecordService{

}




