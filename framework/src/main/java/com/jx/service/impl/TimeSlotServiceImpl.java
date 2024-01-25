package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddTimeSlotDto;
import com.jx.domain.dto.ListTimeSlotDto;
import com.jx.domain.entity.ActivityAssignment;
import com.jx.domain.entity.Location;
import com.jx.domain.entity.Scheduled;
import com.jx.domain.entity.TimeSlot;
import com.jx.domain.vo.PageVo;
import com.jx.domain.vo.TimeSlotVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.TimeSlotMapper;
import com.jx.service.ActivityAssignmentService;
import com.jx.service.TimeSlotService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (TimeSlot)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:25:25
 */
@Service("timeSlotService")
public class TimeSlotServiceImpl extends ServiceImpl<TimeSlotMapper, TimeSlot> implements TimeSlotService {

    @Autowired
    ActivityAssignmentService activityAssignmentService;
    @Override
    public ResponseResult listTimeSlots(Long pageNum, Long pageSize, ListTimeSlotDto listTimeSlotDto) {
        LambdaQueryWrapper<TimeSlot> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(listTimeSlotDto.getStatus()),TimeSlot::getStatus,listTimeSlotDto.getStatus());
        lambdaQueryWrapper.like(StringUtils.hasText(listTimeSlotDto.getBeginTime()),TimeSlot::getBeginTime,listTimeSlotDto.getBeginTime());
        lambdaQueryWrapper.like(StringUtils.hasText(listTimeSlotDto.getEndTime()),TimeSlot::getEndTime,listTimeSlotDto.getEndTime());
        Page<TimeSlot> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<TimeSlotVo> list = BeanCopyUtils.copyBeanList(page.getRecords(),TimeSlotVo.class);
        PageVo pageVo = new PageVo(list,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getTimeSlotDetail(Long id) {
        TimeSlot timeSlot = getById(id);
        TimeSlotVo timeSlotVo = BeanCopyUtils.copyBean(timeSlot,TimeSlotVo.class);
        return ResponseResult.okResult(timeSlotVo);
    }

    @Override
    public ResponseResult deleteTimeSlot(List<Long> timeSlotIds) {
        List<String> ans = new ArrayList<>();
        timeSlotIds.stream().forEach(id->{
            LambdaQueryWrapper<ActivityAssignment> activityAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            activityAssignmentLambdaQueryWrapper.eq(ActivityAssignment::getTimeSlotId,id);
            if (activityAssignmentService.count(activityAssignmentLambdaQueryWrapper) > 0) {
                TimeSlot timeSlot = getById(id);
                ans.add(timeSlot.getBeginTime()+"-"+timeSlot.getEndTime());
            }
            else {
                removeById(id);
            }
        });
        if(ans.size()>0){
            return ResponseResult.errorResult(550,"时间段"+ans.toString()+"有活动使用,无法删除");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTimeSlot(AddTimeSlotDto addTimeSlotDto) {
        TimeUtils.timeJudge(addTimeSlotDto.getBeginTime(),addTimeSlotDto.getEndTime());
        TimeSlot timeSlot = BeanCopyUtils.copyBean(addTimeSlotDto,TimeSlot.class);
        updateById(timeSlot);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addTimeSlot(AddTimeSlotDto addTimeSlotDto) {
        //判断时间
        TimeUtils.timeJudge(addTimeSlotDto.getBeginTime(),addTimeSlotDto.getEndTime());
        LambdaQueryWrapper<TimeSlot> timeSlotLambdaQueryWrapper = new LambdaQueryWrapper<>();
        timeSlotLambdaQueryWrapper.eq(TimeSlot::getBeginTime,addTimeSlotDto.getBeginTime());
        timeSlotLambdaQueryWrapper.eq(TimeSlot::getEndTime,addTimeSlotDto.getEndTime());
        int cnt = count(timeSlotLambdaQueryWrapper);
        if(cnt>0){
            throw new SystemException(AppHttpCodeEnum.TIMESLOT_EXIT);
        }
        TimeSlot timeSlot = BeanCopyUtils.copyBean(addTimeSlotDto,TimeSlot.class);
        save(timeSlot);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTimeSlot() {
        LambdaQueryWrapper<TimeSlot> timeSlotLambdaQueryWrapper = new LambdaQueryWrapper<>();
        timeSlotLambdaQueryWrapper.eq(TimeSlot::getStatus, SystemConstants.STATUS_NORMAL);
        List<TimeSlotVo> timeSlotVos = BeanCopyUtils.copyBeanList(list(timeSlotLambdaQueryWrapper),TimeSlotVo.class);
        return ResponseResult.okResult(timeSlotVos);
    }

}
