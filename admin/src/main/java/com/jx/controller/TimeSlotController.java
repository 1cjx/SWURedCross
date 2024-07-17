package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddTimeSlotDto;
import com.jx.domain.dto.ListTimeSlotDto;
import com.jx.service.TimeSlotService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/timeSlot")
@Api(tags = "活动时间段相关接口")
public class TimeSlotController {
    @Autowired
    TimeSlotService timeSlotService;
    @SystemLog(businessName = "分页查询活动时间段",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:timeSlot:query')")
    public ResponseResult list(Long pageNum, Long pageSize, ListTimeSlotDto listTimeSlotDto){
        return timeSlotService.listTimeSlots(pageNum,pageSize,listTimeSlotDto);
    }
    @SystemLog(businessName = "查询选中活动时间段详情信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:timeSlot:edit')")
    public ResponseResult getTimeSlotDetail(@PathVariable("id") Long id){
        return timeSlotService.getTimeSlotDetail(id);
    }
    @SystemLog(businessName = "更新活动时间段",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:timeSlot:edit')")
    public ResponseResult updateTimeSlot(@RequestBody AddTimeSlotDto addTimeSlotDto){
        return timeSlotService.updateTimeSlot(addTimeSlotDto);
    }
    @SystemLog(businessName = "新增活动时间段",type="2")
    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:timeSlot:add')")
    public ResponseResult addTimeSlot(@RequestBody AddTimeSlotDto addTimeSlotDto){
        return timeSlotService.addTimeSlot(addTimeSlotDto);
    }
    @SystemLog(businessName = "删除选中活动时间段",type="2")
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('content:timeSlot:remove')")
    public ResponseResult deleteTimeSlot(@RequestBody List<Long> timeSlotIds){
        return timeSlotService.deleteTimeSlot(timeSlotIds);
    }
}
