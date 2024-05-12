package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.*;
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

    ResponseResult addSchedule(AddScheduledDto addScheduledDto);

    ResponseResult getActivityDetail(Long activityId);


    ResponseResult userActivityList(Long pageNum, Long pageSize,Boolean type);
    ResponseResult userActivityDetail(Long activityId,Boolean type);

    ResponseResult userVolunteerInfo(Long pageNum, Long pageSize);

    ResponseResult listActivity(Long pageSize, Long pageNum, ListActivityDto listActivityDto);

    ResponseResult getActivityDetailForAdmin(Long id);

    ResponseResult releaseActivity(AddActivityDto addActivityDto);

    ResponseResult updateActivity(UpdateActivityDto updateActivityDto);

    ResponseResult deleteActivity(Long id);


    ResponseResult changeActivityStatus(ChangeActivityStatusDto changeActivityStatusDto);

    ResponseResult cancelSchedule(AddScheduledDto addScheduledDto);

}

