package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.system.domain.SysMenu;
import com.opspanel.system.dto.menu.MenuCreateCmd;
import com.opspanel.system.dto.menu.MenuQuery;
import com.opspanel.system.dto.menu.MenuUpdateCmd;

import java.util.List;

/**
 * Service interface for menu operations.
 */
public interface SysMenuService {
    IPage<SysMenu> page(MenuQuery query, int pageNum, int pageSize);
    Long create(MenuCreateCmd cmd);
    boolean update(MenuUpdateCmd cmd);
    boolean batchDelete(List<Long> ids);
}
