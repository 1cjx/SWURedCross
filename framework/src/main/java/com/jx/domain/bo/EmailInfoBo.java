package com.jx.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    String imgUrl;
    String activityTypeName;
}
