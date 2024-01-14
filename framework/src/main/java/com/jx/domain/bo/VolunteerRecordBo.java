package com.jx.domain.bo;

import com.jx.domain.entity.ActivityAssignment;
import com.jx.domain.vo.ActivityAssignmentVo;
import com.jx.domain.vo.ActivityVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerRecordBo {
    private Long activityAssignmentId;
    private ActivityAssignmentVo activityAssignmentVo;
    private String status;
    private String reason;
    private Double volunteerTime;
}
