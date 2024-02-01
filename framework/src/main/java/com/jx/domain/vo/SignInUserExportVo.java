package com.jx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInUserExportVo {
    private String activityName;
    private Long userId;
    private String userName;
    private String collegeName;
    private String isRedCrossMember;
    private String qq;
    private String phoneNumber;
    private String email;
    private String userNeed;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date signInTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date midTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date signOutTime;
    private Double volunteerTime;
}
