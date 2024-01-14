package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.PostAssignment;
import com.jx.mapper.PostAssignmentMapper;
import com.jx.service.PostAssignmentService;
import org.springframework.stereotype.Service;

/**
 * (PostAssignment)表服务实现类
 *
 * @author makejava
 * @since 2023-11-09 17:08:32
 */
@Service("postAssignmentService")
public class PostAssignmentServiceImpl extends ServiceImpl<PostAssignmentMapper, PostAssignment> implements PostAssignmentService {

}
