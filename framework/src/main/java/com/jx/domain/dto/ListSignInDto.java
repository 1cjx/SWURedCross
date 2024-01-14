package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSignInDto {
    private String activityName;
    private Long locationId;
    private Long timeSlotId;
    private Long typeId;
}
