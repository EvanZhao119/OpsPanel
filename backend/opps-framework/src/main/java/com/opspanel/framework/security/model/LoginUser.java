package com.opspanel.framework.security.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a logged-in user's information.
 */
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique user ID */
    private Long userId;

    /** Username */
    private String username;

    /** Department ID */
    private Long deptId;

    /** Granted permissions */
    private Set<String> permissions;

    /** Authentication token */
    private String token;

    /** Login timestamp */
    private Long loginTime;

    /** Expiration timestamp */
    private Long expireTime;

    public LoginUser() {}

    public LoginUser(Long userId, String username, Long deptId, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.deptId = deptId;
        this.permissions = permissions;
    }

    // --- Getters and Setters ---
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getLoginTime() { return loginTime; }
    public void setLoginTime(Long loginTime) { this.loginTime = loginTime; }

    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
}
