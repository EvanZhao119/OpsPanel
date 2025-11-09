package com.opspanel.framework.security.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents the authenticated user's session information.
 */
public class AuthenticatedUser implements Serializable {
    private Long userId;
    private String username;
    private Long departmentId;
    private Set<String> roles;
    private String token;
    private Long loginTimestamp;
    private Long expirationTimestamp;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(Long loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
