package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCategoryHoldVo {
    private Long categoryId;
    private String categoryName;
    private Long nums;
}
