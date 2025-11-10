package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysMenu;
import com.opspanel.system.dto.menu.MenuCreateCmd;
import com.opspanel.system.dto.menu.MenuQuery;
import com.opspanel.system.dto.menu.MenuUpdateCmd;
import com.opspanel.system.mapper.SysMenuMapper;
import com.opspanel.system.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of SysMenuService using MyBatis-Plus.
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public IPage<SysMenu> page(MenuQuery query, int pageNum, int pageSize) {
        return this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysMenu>()
                        .like(query.getMenuName() != null, SysMenu::getMenuName, query.getMenuName())
                        .eq(query.getParentId() != null, SysMenu::getParentId, query.getParentId())
                        .eq(query.getVisible() != null, SysMenu::getVisible, query.getVisible())
                        .eq(query.getStatus() != null, SysMenu::getStatus, query.getStatus())
                        .orderByAsc(SysMenu::getOrderNum)
        );
    }

    @Override
    public Long create(MenuCreateCmd cmd) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(cmd, menu);
        this.save(menu);
        return menu.getMenuId();
    }

    @Override
    public boolean update(MenuUpdateCmd cmd) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(cmd, menu);
        return this.updateById(menu);
    }

    @Override
    public boolean remove(Long id) {
        return this.removeById(id);
    }
}
