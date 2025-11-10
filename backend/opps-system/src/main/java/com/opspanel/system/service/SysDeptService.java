package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.system.domain.SysDept;
import com.opspanel.system.dto.dept.DeptCreateCmd;
import com.opspanel.system.dto.dept.DeptQuery;
import com.opspanel.system.dto.dept.DeptUpdateCmd;

/**
 * Service interface for department operations.
 */
public interface SysDeptService {
    IPage<SysDept> page(DeptQuery query, int pageNum, int pageSize);
    Long create(DeptCreateCmd cmd);
    boolean update(DeptUpdateCmd cmd);
    boolean remove(Long id);
}
