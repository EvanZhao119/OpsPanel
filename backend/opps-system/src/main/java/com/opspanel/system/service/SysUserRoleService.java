package com.opspanel.system.service;

import java.util.List;

/** Service for managing user-role relationships. */
public interface SysUserRoleService {
    void assignRoles(Long userId, List<Long> roleIds);
    void removeRoles(Long userId);
    List<Long> getRoleIdsByUser(Long userId);
}
