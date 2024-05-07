package com.jx.domain.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Scheduled)表实体类
 *
 * @author makejava
 * @since 2023-09-02 17:25:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_scheduled")
public class Scheduled  {
    //安排id
    private Long postAssignmentId;
    //用户id
    private Long userId;

    private String isSendEmail;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}

