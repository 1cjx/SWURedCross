package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserImportRecord)表实体类
 *
 * @author makejava
 * @since 2023-10-26 17:11:51
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_import_record")
public class UserImportRecord  {
    @TableId
    private Long id;

    
    private Long allNum;
    
    private Long succeedNum;
    
    private Long failNum;
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

}

