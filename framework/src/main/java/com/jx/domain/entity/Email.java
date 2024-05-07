package com.jx.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private Long msgId;
    /**
     * 邮件接收方，可多人
     */
    private List<String> to;
    /**
     * 邮件主题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String content;
    private Long userId;
    private Long postAssignmentId;
}
