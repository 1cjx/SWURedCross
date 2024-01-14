package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.entity.UserImportDetail;
import com.jx.mapper.UserImportDetailMapper;
import com.jx.service.UserImportDetailService;
import org.springframework.stereotype.Service;

/**
 * (UserImportDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-10-26 17:11:41
 */
@Service("userImportDetailService")
public class UserImportDetailServiceImpl extends ServiceImpl<UserImportDetailMapper, UserImportDetail> implements UserImportDetailService {

}
