package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String name;
    /**
     * 部门
     */
    private String department;
    private String college;
    private String role;

    /**
     * 头像
     */
    private String avatar;

    private String sex;
    private String email;

    private String phoneNumber;
    private String qq;
    private String isBind;
}
