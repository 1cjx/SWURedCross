package com.jx.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (VolunteerRecord)表实体类
 *
 * @author makejava
 * @since 2023-11-05 21:17:12
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_volunteer_record")
public class VolunteerRecord  {
    @TableId
    private Long id;

    
    private Long activityAssignmentId;
    
    private Long userId;
    
    private Double volunteerTime;
    
    private String reason;

    private String status;


}

