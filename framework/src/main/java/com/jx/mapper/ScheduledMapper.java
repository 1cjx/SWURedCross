package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.Scheduled;


/**
 * (Scheduled)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:25:13
 */
public interface ScheduledMapper extends BaseMapper<Scheduled> {

    void remove(Scheduled scheduled);
}

