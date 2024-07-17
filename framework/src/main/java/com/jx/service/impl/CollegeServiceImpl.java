package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.College;
import com.jx.mapper.CollegeMapper;
import com.jx.service.CollegeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * (College)表服务实现类
 *
 * @author makejava
 * @since 2023-10-26 21:17:18
 */
@Service("collegeService")
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

    @Override
    public ResponseResult selectColleges(String name) {
        LambdaQueryWrapper<College> collegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        collegeLambdaQueryWrapper.like(StringUtils.hasText(name),College::getName,name);
        List<College> collegeList = list(collegeLambdaQueryWrapper);
        return ResponseResult.okResult(collegeList);
    }

    @Override
    public ResponseResult listAllCollege() {
        List<College> collegeList = list();
        return ResponseResult.okResult(collegeList);
    }
}
