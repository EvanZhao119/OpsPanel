package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.dto.user.UserCreateCmd;
import com.opspanel.system.dto.user.UserQuery;
import com.opspanel.system.dto.user.UserUpdateCmd;

public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> page(UserQuery query, int pageNum, int pageSize);

    Long create(UserCreateCmd cmd);

    boolean update(UserUpdateCmd cmd);

    boolean remove(Long userId);
}
