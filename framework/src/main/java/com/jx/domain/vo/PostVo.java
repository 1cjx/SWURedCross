package com.jx.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    //岗位id
    @TableId
    private Long id;
    //岗位名
    private String name;
    //岗位描述
    private String description;
    //0为通用
    private String categoryName;
    private Long categoryId;
    private String status;
}
