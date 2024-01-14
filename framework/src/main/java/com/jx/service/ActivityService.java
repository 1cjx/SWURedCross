package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddActivityDto;
import com.jx.domain.dto.ListActivityDto;
import com.jx.domain.dto.UpdateActivityDto;
import com.jx.domain.entity.Activity;
import com.jx.domain.entity.Scheduled;


/**
 * (Activity)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:21:00
 */
public interface ActivityService extends IService<Activity> {

    ResponseResult getActivityList(String status,Long locationId,Long categoryId,String activityName,Long pageNum,Long pageSize);

    ResponseResult addSchedule(Scheduled scheduled);

    ResponseResult getActivityDetail(Long activityId);


    ResponseResult userActivityList(Long pageNum, Long pageSize);

    ResponseResult userAsLeaderActivityList(Long pageNum, Long pageSize,String type);

    ResponseResult userVolunteerInfo(Long pageNum, Long pageSize);

    ResponseResult listActivity(Long pageSize, Long pageNum, ListActivityDto listActivityDto);

    ResponseResult getActivityDetailForAdmin(Long id);

    ResponseResult releaseActivity(AddActivityDto addActivityDto);

    ResponseResult updateActivity(UpdateActivityDto updateActivityDto);

    ResponseResult deleteActivity(Long id);

    ResponseResult getActivityAssignmentDetail(Long id);
}

