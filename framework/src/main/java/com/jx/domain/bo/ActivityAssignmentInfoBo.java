package com.jx.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAssignmentInfoBo {
    private Long id;
    private Long timeSlotId;
    private String beginTime;
    private String endTime;
    private Long locationId;
    private String locationName;
    private Date time;
    private Long typeId;
    private String typeName;
    private String status;
    private String qrCodeUrl;
    private String groupAccount;
    private List<PostNeedBo> postList;
}
