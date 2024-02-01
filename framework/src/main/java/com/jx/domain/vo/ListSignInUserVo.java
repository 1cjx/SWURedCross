package com.jx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSignInUserVo {

   private String activityName;
   private String locationName;
   private String timeSlotBegin;
   private String timeSlotEnd;
   private String userName;
   private String time;
   @JSONField(format = "yyyy-MM-dd HH:mm:ss")
   private Date signInTime;

   @JSONField(format = "yyyy-MM-dd HH:mm:ss")
   private Date midTime;
   @JSONField(format = "yyyy-MM-dd HH:mm:ss")
   private Date signOutTime;
}
