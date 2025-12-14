package com.opspanel.admin.dto.auth;

/**
 * Login request payload.
 * Used for user authentication (username + password).
 */
public record LoginRequest(String username, String password) { }
