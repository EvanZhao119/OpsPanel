package com.opspanel.admin.dto.auth;

/**
 * Token refresh request payload.
 * Used to request a new access token using a valid refresh token.
 */
public record TokenRefreshRequest(String refreshToken) { }
