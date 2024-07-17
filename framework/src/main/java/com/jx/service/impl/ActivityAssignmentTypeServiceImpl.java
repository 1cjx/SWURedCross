package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ActivityAssignmentType;
import com.jx.domain.vo.PageVo;
import com.jx.mapper.ActivityAssignmentTypeMapper;
import com.jx.service.ActivityAssignmentTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * (ActivityAssignmentType)表服务实现类
 *
 * @author makejava
 * @since 2023-11-24 16:35:03
 */
@Service("activityAssignmentTypeService")
public class ActivityAssignmentTypeServiceImpl extends ServiceImpl<ActivityAssignmentTypeMapper, ActivityAssignmentType> implements ActivityAssignmentTypeService {

    @Override
    public ResponseResult listActivityAssignmentType(Long pageSize, Long pageNum, String name) {
        LambdaQueryWrapper<ActivityAssignmentType> activityAssignmentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityAssignmentTypeLambdaQueryWrapper.eq(StringUtils.hasText(name),ActivityAssignmentType::getName,name);
        Page<ActivityAssignmentType>page = new Page();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,activityAssignmentTypeLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult listAllActivityAssignmentType() {
        List<ActivityAssignmentType> activityAssignmentTypeList = list();
        return ResponseResult.okResult(activityAssignmentTypeList);
    }
}
