package com.opspanel.admin.controller;

import com.opspanel.admin.dto.auth.LoginRequest;
import com.opspanel.admin.dto.auth.TokenRefreshRequest;
import com.opspanel.framework.security.JwtTokenProvider;
import com.opspanel.framework.security.model.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller for login and token refresh.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Login endpoint — authenticates user and returns JWT tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        // Build access & refresh tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), Map.of("role", user.getAuthorities()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }

    /**
     * Token refresh endpoint.
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();
        if (jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.isRefreshToken(refreshToken)) {
            String username = jwtTokenProvider.getUsername(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(username, Map.of());
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired refresh token"));
    }

    /**
     * Check current authentication status.
     */
    @GetMapping("/me")
    public ResponseEntity<?> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof AuthenticatedUser user) {
            return ResponseEntity.ok(Map.of(
                    "username", user.getUsername(),
                    "roles", user.getAuthorities()
            ));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
    }
}
