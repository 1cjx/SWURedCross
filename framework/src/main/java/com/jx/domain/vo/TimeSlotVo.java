package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotVo {
    //时间段id
    private Long id;

    //起始时间
    private String beginTime;
    //结束时间
    private String endTime;
}
