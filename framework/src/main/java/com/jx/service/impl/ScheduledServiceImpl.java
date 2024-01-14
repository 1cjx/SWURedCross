package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.Scheduled;
import com.jx.mapper.ScheduledMapper;
import com.jx.service.ScheduledService;
import org.springframework.stereotype.Service;

/**
 * (Scheduled)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:25:13
 */
@Service("scheduledService")
public class ScheduledServiceImpl extends ServiceImpl<ScheduledMapper, Scheduled> implements ScheduledService {

}
