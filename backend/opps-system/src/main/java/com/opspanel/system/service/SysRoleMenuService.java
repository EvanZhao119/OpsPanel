package com.opspanel.system.service;

import java.util.List;

/** Service for managing role-menu relationships. */
public interface SysRoleMenuService {
    void assignMenus(Long roleId, List<Long> menuIds);
    void removeMenus(Long roleId);
    List<Long> getMenuIdsByRole(Long roleId);
}
