package com.jx.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddScheduledDto  {
    //活动id
    private Long activityId;
    //班次id
    private Long activityAssignmentId;
    //岗位安排id
    private Long postAssignmentId;
    //用户id
    private Long userId;




}