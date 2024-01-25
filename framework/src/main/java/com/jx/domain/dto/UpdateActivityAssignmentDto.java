package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActivityAssignmentDto {
    private Long id;
    private Date time;
    private Long locationId;
    private String  status;
    private Long timeSlotId;
    private Long typeId;
    private List<UpdatePostAssignmentDto> postList;
}
