package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddActivityDto;
import com.jx.domain.dto.ChangeActivityStatusDto;
import com.jx.domain.dto.ListActivityDto;
import com.jx.domain.dto.UpdateActivityDto;
import com.jx.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "活动相关接口")
@RequestMapping("/system/activity")
@RestController
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @ApiOperation("分页查询活动")
    @GetMapping("/list")
    public ResponseResult listActivity(Long pageSize, Long pageNum, ListActivityDto listActivityDto){
        return activityService.listActivity(pageSize,pageNum,listActivityDto);
    }
    @ApiOperation("查询选中活动详情信息")
    @GetMapping("/{id}")
    public ResponseResult getActivityDetail(@PathVariable("id") Long id){
        return activityService.getActivityDetailForAdmin(id);
    }
    @ApiOperation("查询选中班次详情信息")
    @GetMapping("/assignment/{id}")
    public ResponseResult getActivityAssignmentDetail(@PathVariable("id") Long id){
        return activityService.getActivityAssignmentDetail(id);
    }
    @ApiOperation("发布活动")
    @PostMapping
    public ResponseResult releaseActivity(@RequestBody AddActivityDto addActivityDto){
        return activityService.releaseActivity(addActivityDto);
    }
    @ApiOperation("更新活动")
    @PutMapping
    public ResponseResult updateActivity(@RequestBody UpdateActivityDto updateActivityDto){
        return activityService.updateActivity(updateActivityDto);
    }
    @ApiOperation("删除选中活动")
    @DeleteMapping("/{id}")
    public ResponseResult deleteActivity(@PathVariable("id")Long id){
        return activityService.deleteActivity(id);
    }

    @ApiOperation("修改活动状态")
    @PutMapping("/changeActivityStatus")
    public ResponseResult changeActivityStatus(@RequestBody ChangeActivityStatusDto changeActivityStatusDto){
        return activityService.changeActivityStatus(changeActivityStatusDto);
    }
}
