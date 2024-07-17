package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ActivityAssignmentType;


/**
 * (ActivityAssignmentType)表服务接口
 *
 * @author makejava
 * @since 2023-11-24 16:35:03
 */
public interface ActivityAssignmentTypeService extends IService<ActivityAssignmentType> {

    ResponseResult listActivityAssignmentType(Long pageSize, Long pageNum, String name);

    ResponseResult listAllActivityAssignmentType();

}

