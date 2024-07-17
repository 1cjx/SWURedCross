package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddActivityDto;
import com.jx.domain.dto.ChangeActivityStatusDto;
import com.jx.domain.dto.ListActivityDto;
import com.jx.domain.dto.UpdateActivityDto;
import com.jx.service.ActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "活动相关接口")
@RequestMapping("/system/activity")
@RestController
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @GetMapping("/list")
    @SystemLog(businessName = "分页查询活动",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:query')")
    public ResponseResult listActivity(Long pageSize, Long pageNum, ListActivityDto listActivityDto){
        return activityService.listActivity(pageSize,pageNum,listActivityDto);
    }
    @GetMapping("/getChatActivityList")
    @SystemLog(businessName = "查询chat可供选择的活动",type="2")
    @PreAuthorize("@ps.hasPermission('content:chat:queryActvity')")
    public ResponseResult getChatActivityList(Long pageNum, Long pageSize, ListActivityDto listActivityDto){
        return activityService.getChatActivityList(pageNum,pageSize,listActivityDto);
    }
    @GetMapping("/{id}")
    @SystemLog(businessName = "查询选中活动详情信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:getDetail')")
    public ResponseResult getActivityDetail(@PathVariable("id") Long id){
        return activityService.getActivityDetailForAdmin(id);
    }
    @PostMapping
    @SystemLog(businessName = "发布活动",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:add')")
    public ResponseResult releaseActivity(@RequestBody AddActivityDto addActivityDto){
        return activityService.releaseActivity(addActivityDto);
    }
    @PutMapping
    @SystemLog(businessName = "更新活动",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:edit')")
    public ResponseResult updateActivity(@RequestBody UpdateActivityDto updateActivityDto){
        return activityService.updateActivity(updateActivityDto);
    }
    @DeleteMapping("/{id}")
    @SystemLog(businessName = "删除选中活动",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:removeZ')")
    public ResponseResult deleteActivity(@PathVariable("id")Long id){
        return activityService.deleteActivity(id);
    }

    @PutMapping("/changeActivityStatus")
    @SystemLog(businessName = "修改活动状态",type="2")
    @PreAuthorize("@ps.hasPermission('content:activity:changeStatus')")
    public ResponseResult changeActivityStatus(@RequestBody ChangeActivityStatusDto changeActivityStatusDto){
        return activityService.changeActivityStatus(changeActivityStatusDto);
    }
}
