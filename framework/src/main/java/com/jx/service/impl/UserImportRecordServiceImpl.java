package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.UserImportRecord;
import com.jx.mapper.UserImportRecordMapper;
import com.jx.service.UserImportRecordService;
import org.springframework.stereotype.Service;

/**
 * (UserImportRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-10-26 17:11:51
 */
@Service("userImportRecordService")
public class UserImportRecordServiceImpl extends ServiceImpl<UserImportRecordMapper, UserImportRecord> implements UserImportRecordService {

}
