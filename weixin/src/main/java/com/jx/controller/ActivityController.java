package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddScheduledDto;
import com.jx.domain.dto.ListActivityDto;
import com.jx.service.ActivityService;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@Api(tags = "活动相关接口")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    /**
     * 分页返回志愿活动列表
     * 默认查询所有招募中的活动按时间排序
     * 可以选择活动类型、地点进行筛选
     * DepartmentId表示仅该部门人员可以访问
     * @param pageNum
     * @param pageSize
     * @param listActivityDto
     * @return
     */
    @SystemLog(businessName = "分页查询活动列表",type="1")
    @GetMapping("/getActivityList")
    public ResponseResult getActivityList(Long pageNum, Long pageSize, ListActivityDto listActivityDto){
        return activityService.getActivityList(pageNum,pageSize,listActivityDto);
    }

    /**
     * 根据活动排班的id与用户的id 绑定排班
     * @param addScheduledDto
     * @return
     */
    @SystemLog(businessName = "报名活动班次",type="1")
    @PostMapping("/addSchedule")
    public ResponseResult addSchedule(@RequestBody AddScheduledDto addScheduledDto){
        return activityService.addSchedule(addScheduledDto);
    }

    @SystemLog(businessName = "取消报名班次",type="1")
    @PostMapping("/cancelSchedule")
    public ResponseResult cancelSchedule(@RequestBody AddScheduledDto addScheduledDto){
        return activityService.cancelSchedule(addScheduledDto);
    }

    /**
     * 根据活动id获取活动详情以及排班信息
     * @param activityId
     * @return
     */
    @SystemLog(businessName = "查询选中活动详情信息",type="1")
    @GetMapping("/getActivityDetail")
    public ResponseResult getActivityDetail(Long activityId){
        return activityService.getActivityDetail(activityId);
    }

    /**
     * 根据名称模糊搜索活动，前端传入查询的名称
     * @param listActivityDto
     * @return
     */
    @SystemLog(businessName = "根据活动名模糊查询活动",type="1")
    @GetMapping("/selectActivity")
    public ResponseResult selectActivity( Long pageNum,Long pageSize, ListActivityDto listActivityDto){
        return activityService.getActivityList(pageNum,pageSize,listActivityDto);
    }

    /**
     * 分页获取用户参与的活动信息,仅包括活动不查询到具体班次
     * @param pageNum 当前页号
     * @param pageSize 页大小
     * @param type type=true查询用户作为班次负责人的活动
     * @return
     */
    @SystemLog(businessName = "分页查询用户参与的活动",type="1")
    @GetMapping("/userActivityList")
    public ResponseResult userActivityList(@Param("pageNum")Long pageNum,@Param("pageSize")Long pageSize,@Param("type")Boolean type){
        return activityService.userActivityList(pageNum,pageSize,type);
    }

    /**
     * 获取用户当前活动报名的班次信息
     * @param activityId 活动id
     * @param type type为false 查询各班次负责人的信息，若为负责人则不查询负责人信息
     *            type=true 查询各班负责人管理的志愿者信息
     * @return
     */
    @SystemLog(businessName = "查询用户当前活动的班次信息",type="1")
    @GetMapping("/userActivityDetail")
    public ResponseResult userActivityDetail(@Param("activityId")Long activityId,@Param("type")Boolean type){
        return activityService.userActivityDetail(activityId,type);
    }
    @SystemLog(businessName = "查询用户已参加活动的情况",type="1")
    @GetMapping("/userVolunteerInfo")
    public ResponseResult userVolunteerInfo(@Param("pageNum")Long pageNum,@Param("pageSize")Long pageSize){
        return activityService.userVolunteerInfo(pageNum,pageSize);
    }
}
