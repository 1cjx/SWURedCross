package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Title)表实体类
 *
 * @author makejava
 * @since 2024-02-01 17:13:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_title")
public class Title  {
    @TableId
    private Long id;

    //职称名
    private String name;
    
    private String status;
    
    private Date createTime;
    
    private Long createBy;
    
    private Date updateTime;
    
    private Long updateBy;
    
    private String delFlag;



}

