package com.jx.domain.entity;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ChatSession)表实体类
 *
 * @author makejava
 * @since 2024-07-14 19:32:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_chat_session")
public class ChatSession  {
    @TableId
    private Long id;

    
    private Long userId;

    private String name;
    private Date createTime;

}

