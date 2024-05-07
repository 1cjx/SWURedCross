package com.jx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRoleVo {
    private Long id;

    private String titleName;
    private String departmentName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    private String remark;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
