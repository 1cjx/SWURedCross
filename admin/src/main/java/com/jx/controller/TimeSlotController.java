package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddPostDto;
import com.jx.domain.dto.AddTimeSlotDto;
import com.jx.domain.dto.ListTimeSlotDto;
import com.jx.service.TimeSlotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/system/timeSlot")
@Api(tags = "活动时间段相关接口")
public class TimeSlotController {
    @Autowired
    TimeSlotService timeSlotService;
    @ApiOperation("分页查询活动时间段")
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, ListTimeSlotDto listTimeSlotDto){
        return timeSlotService.listTimeSlots(pageNum,pageSize,listTimeSlotDto);
    }
    @ApiOperation("查询选中活动时间段详情信息")
    @GetMapping("/{id}")
    public ResponseResult getTimeSlotDetail(@PathVariable("id") Long id){
        return timeSlotService.getTimeSlotDetail(id);
    }
    @ApiOperation("更新活动时间段")
    @PutMapping
    public ResponseResult updateTimeSlot(@RequestBody AddTimeSlotDto addTimeSlotDto){
        return timeSlotService.updateTimeSlot(addTimeSlotDto);
    }
    @ApiOperation("新增活动时间段")
    @PostMapping
    public ResponseResult addTimeSlot(@RequestBody AddTimeSlotDto addTimeSlotDto){
        return timeSlotService.addTimeSlot(addTimeSlotDto);
    }
    @ApiOperation("删除选中活动时间段")
    @DeleteMapping("{id}")
    public ResponseResult deleteTimeSlot(@PathVariable("id") Long id){
        return timeSlotService.deleteTimeSlot(id);
    }
}
