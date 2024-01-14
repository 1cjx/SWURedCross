package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostAssignmentDto {
    private Long id;
    private Long postId;
    private Long reqPeople;
    private Long allowedDepartmentId;
    private Long allowedRoleId;
}
