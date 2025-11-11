package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.dto.user.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Mapper interface for system user operations.
 * <p>
 * Extends {@link BaseMapper} to inherit basic CRUD operations.
 * Custom queries can be defined here when business logic
 * goes beyond the default MyBatis-Plus capabilities.
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * Retrieve a user by username.
     *
     * @param username the username to look up
     * @return the matching {@link SysUser} entity, or null if not found
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * Custom conditional query for user list.
     * This method serves as a replacement for the former XML-based query.
     *
     * @param query the query parameters encapsulated in {@link UserQuery}
     * @return a list of users matching the specified conditions
     */
    List<SysUser> selectByConditions(@Param("q") UserQuery query);

    @Select("""
    SELECT r.role_key
    FROM sys_role r
    INNER JOIN sys_user_role ur ON r.role_id = ur.role_id
    WHERE ur.user_id = #{userId}
""")
    Set<String> selectRoleKeysByUserId(@Param("userId") Long userId);
}
