package com.jx.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailInfoBo {
    String userName;
    String activityName;
    Date activityDate;
    String beginTime;
    String endTime;
    String postName;
    String locationName;
    String qrCodeUrl;
    String groupAccount;
    String activityTypeName;
}
