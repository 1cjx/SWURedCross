package com.jx.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ActivityAssignmentType)表实体类
 *
 * @author makejava
 * @since 2023-11-24 16:35:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_activity_assignment_type")
public class ActivityAssignmentType  {
    @TableId
    private Long id;
    private String name;
    private String description;


}

