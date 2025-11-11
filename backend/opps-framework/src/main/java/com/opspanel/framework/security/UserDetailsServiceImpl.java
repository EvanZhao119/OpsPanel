package com.opspanel.framework.security;

import com.opspanel.common.constant.UserConstants;
import com.opspanel.framework.security.model.AuthenticatedUser;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserDetailsServiceImpl
 * -------------------------------------------------------------
 * This class is used by Spring Security to load user information
 * from the database when a user attempts to log in.
 *
 * Responsibilities:
 *  1. Query user information by username.
 *  2. Validate account status (active, disabled, deleted).
 *  3. Load user roles and convert them into GrantedAuthorities.
 *  4. Wrap SysUser into an AuthenticatedUser object.
 *  5. Return a UserDetails object to Spring Security.
 *
 * Note: Password verification is handled automatically by
 * Spring Security's AuthenticationProvider using the configured
 * PasswordEncoder.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /** Mapper for querying user and role data */
    private final SysUserMapper userMapper;

    /**
     * Loads user details by username.
     *
     * @param username the username from login form
     * @return a UserDetails object containing authentication information
     * @throws UsernameNotFoundException if the user does not exist or is inactive
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Step 1. Query user from database
        SysUser sysUser = userMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Step 2. Check account status using constants (avoid magic numbers)
        if (UserConstants.Status.DISABLED.equals(sysUser.getStatus())) {
            throw new UsernameNotFoundException("User is disabled: " + username);
        }
        if (UserConstants.Status.DELETED.equals(sysUser.getStatus())) {
            throw new UsernameNotFoundException("User is deleted: " + username);
        }

        // Step 3. Load user roles and map them to GrantedAuthorities
        Set<String> roleKeys = userMapper.selectRoleKeysByUserId(sysUser.getUserId());
        Collection<? extends GrantedAuthority> authorities = roleKeys.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toSet());

        // Step 4. Build AuthenticatedUser object
        AuthenticatedUser user = new AuthenticatedUser();
        user.setUserId(sysUser.getUserId());
        user.setUsername(sysUser.getUsername());
        user.setPassword(sysUser.getPassword()); // already encrypted in DB
        user.setDepartmentId(sysUser.getDeptId());
        user.setRoles(roleKeys);
        user.setAuthorities(authorities);
        user.setLoginTimestamp(System.currentTimeMillis());
        user.setExpirationTimestamp(System.currentTimeMillis() + 3600_000L);

        // Step 5. Return to Spring Security
        return user;
    }
}
