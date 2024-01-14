package com.jx.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelSignInUserVo {
    @ExcelProperty("活动名")
    private String activityName;
    @ExcelProperty("地点")
    private String locationName;
    @ExcelProperty("时间段")
    private String timeSlot;
    @ExcelProperty("姓名")
    private String userName;
    @ExcelProperty("签到类型")
    private String signInType;
    @ExcelProperty("签到时间")
    private Date signInTime;
    private String timeSlotBegin;
    private String timeSlotEnd;
    private Long signInTypeId;
}
