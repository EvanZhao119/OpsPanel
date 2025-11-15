package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.system.domain.SysRole;
import com.opspanel.system.dto.role.RoleCreateCmd;
import com.opspanel.system.dto.role.RoleQuery;
import com.opspanel.system.dto.role.RoleUpdateCmd;

import java.util.List;

public interface SysRoleService {
    IPage<SysRole> page(RoleQuery query, int pageNum, int pageSize);
    Long create(RoleCreateCmd cmd);
    boolean update(RoleUpdateCmd cmd);
    boolean batchRemove(List<Long> ids);
}
