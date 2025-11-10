package com.opspanel.admin.controller;

import com.opspanel.common.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Simple authentication simulation for now (JWT integration later).
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestParam String username, @RequestParam String password) {
        log.info("Login attempt: {}", username);
        // TODO: integrate JWT authentication
        return ApiResponse.ok("Login successful (mock)");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.ok("Logout successful");
    }
}
