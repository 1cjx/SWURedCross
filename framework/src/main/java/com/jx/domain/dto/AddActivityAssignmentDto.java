package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddActivityAssignmentDto {
    private Date time;
    private Long locationId;
    private Long timeSlotId;
    private Long typeId;
    private List<AddPostAssignmentDto> postList;
}
