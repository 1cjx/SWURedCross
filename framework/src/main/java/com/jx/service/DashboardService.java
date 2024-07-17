package com.jx.service;

import com.jx.domain.bean.ResponseResult;

public interface DashboardService {
    ResponseResult getVolunteerNums();

    ResponseResult getTotalVolunteerTimes();

    ResponseResult getActivityNums();

    ResponseResult getRankingByActivity(Long departmentId, Long sortType);

    ResponseResult getVariousActivitiesNum();
}
