package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Location)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:24:23
 */
public interface LocationMapper extends BaseMapper<Location> {
    List<Location> getActivityLocations(@Param("activityId") Long activityId);
}

