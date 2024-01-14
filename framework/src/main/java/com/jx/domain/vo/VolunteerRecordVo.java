package com.jx.domain.vo;

import com.jx.domain.bo.VolunteerRecordBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerRecordVo {
    private Long activityId;
    private ActivityVo activityVo;
    private List<VolunteerRecordBo> volunteerRecordBoList;
    private Double TotalVolunteerTime;
}
