package com.jx.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ChatRoleContent)表实体类
 *
 * @author makejava
 * @since 2024-07-14 19:30:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_chat_role_content")
public class ChatRoleContent  {
    @TableId
    private Long id;

    
    private String role;
    
    private String content;
    
    private Long sessionId;



}

