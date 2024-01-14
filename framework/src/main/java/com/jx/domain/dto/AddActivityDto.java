package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddActivityDto {
    private String name;
    private Long categoryId;
    private Long allowedDepartmentId;
    private String status;
    private String theme;
    private Date beginDate;
    private Date endDate;
    private List<AddActivityAssignmentDto> activityAssignmentList;
}
