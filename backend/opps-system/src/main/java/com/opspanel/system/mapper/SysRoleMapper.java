package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/** Mapper for system roles. */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT * FROM sys_role WHERE role_name = #{roleName} AND deleted = 0")
    SysRole selectByName(@Param("roleName") String roleName);
}
