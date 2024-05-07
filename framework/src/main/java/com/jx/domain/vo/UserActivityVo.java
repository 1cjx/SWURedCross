package com.jx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jx.domain.bo.PostNeedBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityVo {
    ActivityAssignmentVo activityInfo;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    Date createTime;
    String myPost;
    UserInfoVo leaderInfo;
    List<PostNeedBo> volunteersInfo;
}
