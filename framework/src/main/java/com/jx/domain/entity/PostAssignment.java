package com.jx.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (PostAssignment)表实体类
 *
 * @author makejava
 * @since 2023-11-09 17:08:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post_assignment")
public class PostAssignment  {
    @TableId
    private Long id;

    
    private Long activityAssignmentId;
    
    private Long postId;
    
    private Long peopleNumber;
    //为0表示没有限制
    private Long allowedDepartmentId;
    //为0表示没有限制
    private Long allowedTitleId;



}

