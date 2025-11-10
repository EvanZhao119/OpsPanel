package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.dto.user.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MyBatis mapper for system user table operations.
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectById(@Param("userId") Long userId);
    SysUser selectByUsername(@Param("username") String username);
    List<SysUser> selectList(@Param("q") UserQuery query);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(@Param("userId") Long userId);
}
