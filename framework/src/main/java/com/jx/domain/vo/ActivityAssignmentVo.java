package com.jx.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAssignmentVo {
    private Long activityAssignmentId;
    private String activityName;
    private Long activityId;
    private String timeSlotBegin;
    private String timeSlotEnd;
    private String location;
    private String type;
    private Date date;
}
