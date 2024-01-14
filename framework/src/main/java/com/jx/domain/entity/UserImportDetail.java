package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserImportDetail)表实体类
 *
 * @author makejava
 * @since 2023-10-26 17:11:40
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_import_detail")
public class UserImportDetail  {
    @TableId
    private Long id;
    private Long recordId;
    private String name;
    private String sex;
    private String phoneNumber;
    
    private String qq;
    
    private String email;

    private String departmentName;
    
    private String status;
    
    private String reason;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
}

