package com.opspanel.framework.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the authenticated user's session information.
 * -------------------------------------------------------------
 * This class implements Spring Security's UserDetails interface.
 * It stores user identity, authorities, roles, and metadata for
 * authentication and authorization.
 *
 * Responsibilities:
 *  - Bridge between SysUser (DB entity) and Spring Security.
 *  - Provide username, password, and authorities.
 *  - Hold session-related metadata such as token and timestamps.
 */
public class AuthenticatedUser implements UserDetails, Serializable {

    /** Unique user ID */
    private Long userId;

    /** Username used for authentication */
    private String username;

    /** Hashed password (encrypted in the database) */
    private String password;

    /** Department ID of the user */
    private Long departmentId;

    /** Raw role keys from the system (e.g., admin, user) */
    private Set<String> roles;

    /** Granted authorities used by Spring Security */
    private Collection<? extends GrantedAuthority> authorities;

    /** Token (JWT access token or similar) */
    private String token;

    /** Login timestamp (in milliseconds) */
    private Long loginTimestamp;

    /** Token expiration timestamp (in milliseconds) */
    private Long expirationTimestamp;

    // ================================================================
    // Getters & Setters
    // ================================================================

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /** Sets the encrypted password from the database */
    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
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

    // ================================================================
    // Implementation of UserDetails Interface
    // ================================================================

    /**
     * Always returns true since account expiration is not enforced here.
     * You may extend this later for policy-based account management.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Always returns true since account locking is not enforced here.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Always returns true since password expiration is not enforced here.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Always returns true unless you plan to disable specific accounts.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // ================================================================
    // Utility Methods
    // ================================================================

    /**
     * Converts the current authorities into a readable string (for logging/debugging).
     */
    @Override
    public String toString() {
        String rolesDisplay = (roles != null)
                ? roles.stream().collect(Collectors.joining(", "))
                : "none";
        return "AuthenticatedUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", roles=" + rolesDisplay +
                '}';
    }
}
