package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.College;
import org.apache.ibatis.annotations.Param;


/**
 * (College)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-26 21:17:16
 */
public interface CollegeMapper extends BaseMapper<College> {

    Long selectByName(@Param("collegeName") String collegeName);
}

