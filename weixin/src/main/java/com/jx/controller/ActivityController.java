package com.jx.controller;

import com.jx.anatation.ApiIdempotent;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.entity.Scheduled;
import com.jx.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.Null;
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
     * @param status
     * @param locationId
     * @param categoryId
     * @return
     */
    @ApiOperation("分页查询活动列表")
    @GetMapping("/getActivityList")
    public ResponseResult getActivityList(@Param("status") String status, @Param("locationId") Long locationId,
                                          @Param("categoryId") Long categoryId,
                                          @Param("pageNum") Long pageNum,
                                          @Param("pageSize") Long pageSize){
        return activityService.getActivityList(status,locationId,categoryId,null,pageNum,pageSize);
    }

    /**
     * 根据活动排班的id与用户的id 绑定排班
     * @param scheduled
     * @return
     */
    @ApiOperation("报名活动班次")
    @PostMapping("/addSchedule")
    public ResponseResult addSchedule(@RequestBody Scheduled scheduled){
        return activityService.addSchedule(scheduled);
    }

    /**
     * 根据活动id获取活动详情以及排班信息
     * @param activityId
     * @return
     */
    @ApiOperation("查询选中活动详情信息")
    @GetMapping("/getActivityDetail")
    public ResponseResult getActivityDetail(Long activityId){
        return activityService.getActivityDetail(activityId);
    }

    /**
     * 根据名称模糊搜索活动，前端传入查询的名称
     * @param activityName
     * @return
     */
    @ApiOperation("根据活动名模糊查询活动")
    @GetMapping("/selectActivity")
    public ResponseResult selectActivity( @Param("pageNum") Long pageNum,
                                          @Param("pageSize") Long pageSize,
                                          @Param("activityName") String activityName){
        return activityService.getActivityList(SystemConstants.STATUS_NORMAL,null,null,activityName,pageNum,pageSize);
    }

    /**
     * 分页获取用户参与的活动信息，同时查询该班负责人的信息
     * 若为负责人 则不查询负责人信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查询用户参与的活动")
    @GetMapping("/userActivityList")
    public ResponseResult userActivityList(@Param("pageNum")Long pageNum,@Param("pageSize")Long pageSize){
        return activityService.userActivityList(pageNum,pageSize);
    }

    /**
     *
     * type为 1表示获取用户作为负责人的活动，查询参与该班志愿者的信息
     *  否则表示获取用户作为负责人的活动列表，用于创建签到使用
     * @return
     */
    @ApiOperation(value="查询用户作为负责人的活动信息",notes = "type为 1表示获取用户作为负责人的活动，查询参与该班志愿者的信息\n" +
            " 否则表示获取用户作为负责人的活动列表，用于创建签到使用")
    @GetMapping("/userAsLeaderActivityList")
    public ResponseResult userAsLeaderActivityList(@Param("pageNum")Long pageNum,@Param("pageSize")Long pageSize,@Param("type")String type){
        return activityService.userAsLeaderActivityList(pageNum,pageSize,type);
    }
    @ApiOperation("查询用户已参加活动的情况")
    @GetMapping("/userVolunteerInfo")
    public ResponseResult userVolunteerInfo(@Param("pageNum")Long pageNum,@Param("pageSize")Long pageSize){
        return activityService.userVolunteerInfo(pageNum,pageSize);
    }
}
