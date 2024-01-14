package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTimeSlotDto {
    private String beginTime;
    private String endTime;
    private String status;
}
