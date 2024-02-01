package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.entity.Activity;
import com.jx.domain.entity.User;
import com.jx.domain.vo.ActivityCategoryHoldVo;
import com.jx.domain.vo.UserVolunteerRecordVo;
import com.jx.mapper.ActivityMapper;
import com.jx.mapper.VolunteerRecordMapper;
import com.jx.service.ActivityService;
import com.jx.service.DashboardService;
import com.jx.service.UserService;
import com.jx.service.VolunteerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DashboardService")
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    UserService userService;
    @Autowired
    VolunteerRecordMapper volunteerRecordMapper;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public ResponseResult getVolunteerNums() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getIsBind,SystemConstants.USER_IS_BINDING);
        int count = userService.count(lambdaQueryWrapper);
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult getTotalVolunteerTimes() {
       Double totalVolunteerTimes =  volunteerRecordMapper.getTotalVolunteerTimes();
        return ResponseResult.okResult(totalVolunteerTimes);
    }

    @Override
    public ResponseResult getActivityNums() {
        LambdaQueryWrapper<Activity> activityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityLambdaQueryWrapper.eq(Activity::getStatus, SystemConstants.STATUS_NORMAL);
        int count = activityService.count(activityLambdaQueryWrapper);
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult getRankingByActivity(Long departmentId, Long sortType) {
        List<UserVolunteerRecordVo>volunteerRecordVos = volunteerRecordMapper.getUserVolunteerRecordList(departmentId,sortType);
        return ResponseResult.okResult(volunteerRecordVos);
    }

    @Override
    public ResponseResult getVariousActivitiesNum() {
        List<ActivityCategoryHoldVo> activityCategoryHoldVos = activityMapper.getVariousActivitiesNum();
        return ResponseResult.okResult(activityCategoryHoldVos);
    }
}
