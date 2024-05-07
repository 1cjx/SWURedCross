package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPostAssignmentDto {
    private Long postId;
    private Long reqPeople;
    private Long allowedDepartmentId;
    private Long allowedTitleId;
}
