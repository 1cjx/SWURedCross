package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.VolunteerRecord;
import com.jx.mapper.VolunteerRecordMapper;
import com.jx.service.VolunteerRecordService;
import org.springframework.stereotype.Service;

/**
 * (VolunteerRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-11-05 21:17:12
 */
@Service("volunteerRecordService")
public class VolunteerRecordServiceImpl extends ServiceImpl<VolunteerRecordMapper, VolunteerRecord> implements VolunteerRecordService {

}
