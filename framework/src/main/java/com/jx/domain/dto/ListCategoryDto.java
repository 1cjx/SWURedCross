package com.jx.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoryDto {

    //分类类型
    private String name;
    //状态(0停用,1正常)
    private String status;
}
