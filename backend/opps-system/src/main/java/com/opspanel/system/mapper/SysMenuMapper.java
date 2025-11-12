package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper interface for menu operations.
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * Retrieve a menu by name.
     *
     * @param menuName menu name
     * @return the matching menu entity or null if not found
     */
    @Select("SELECT * FROM sys_menu WHERE menu_name = #{menuName} AND deleted = 0")
    SysMenu selectByName(@Param("menuName") String menuName);
}
