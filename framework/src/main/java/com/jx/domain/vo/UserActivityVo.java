package com.jx.domain.vo;

import com.jx.domain.bo.PostNeedBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityVo {
    ActivityAssignmentVo activityInfo;
    String myPost;
    UserInfoVo leaderInfo;
    List<PostNeedBo> volunteersInfo;
}
