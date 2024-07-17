package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddTimeSlotDto;
import com.jx.domain.dto.ListTimeSlotDto;
import com.jx.domain.entity.TimeSlot;

import java.util.List;


/**
 * (TimeSlot)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:25:25
 */
public interface TimeSlotService extends IService<TimeSlot> {

    ResponseResult listTimeSlots(Long pageNum, Long pageSize, ListTimeSlotDto listTimeSlotDto);

    ResponseResult getTimeSlotDetail(Long id);

    ResponseResult deleteTimeSlot(List<Long> timeSlotIds);

    ResponseResult updateTimeSlot(AddTimeSlotDto addTimeSlotDto);

    ResponseResult addTimeSlot(AddTimeSlotDto addTimeSlotDto);

    ResponseResult listAllTimeSlot();
}

