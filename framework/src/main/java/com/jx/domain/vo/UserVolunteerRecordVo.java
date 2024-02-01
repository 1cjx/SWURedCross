package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVolunteerRecordVo {
    private Long userId;
    private String userName;
    private String departmentName;
    private Long userParticipateInActivityNum;
    private Double userVolunteerTimes;
}
