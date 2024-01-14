package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.ActivityAssignment;
import com.jx.mapper.ActivityAssignmentMapper;
import com.jx.service.ActivityAssignmentService;
import org.springframework.stereotype.Service;

/**
 * (ActivityAssignment)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:23:35
 */
@Service("activityAssignmentService")
public class ActivityAssignmentServiceImpl extends ServiceImpl<ActivityAssignmentMapper, ActivityAssignment> implements ActivityAssignmentService {

}
