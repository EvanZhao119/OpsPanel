package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysRoleMenu;
import com.opspanel.system.mapper.SysRoleMenuMapper;
import com.opspanel.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation of SysRoleMenuService. */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
        implements SysRoleMenuService {

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        // Remove existing relations
        this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // Batch insert new ones
        List<SysRoleMenu> relations = menuIds.stream()
                .map(mid -> {
                    SysRoleMenu rm = new SysRoleMenu();
                    rm.setRoleId(roleId);
                    rm.setMenuId(mid);
                    return rm;
                }).collect(Collectors.toList());
        this.saveBatch(relations);
    }

    @Override
    @Transactional
    public void removeMenus(Long roleId) {
        this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
    }

    @Override
    public List<Long> getMenuIdsByRole(Long roleId) {
        return this.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }
}
