package com.opspanel.admin.controller;

import com.opspanel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic health-check and welcome endpoint.
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public ApiResponse<String> index() {
        return ApiResponse.ok("Welcome to OpsPanel Backend!");
    }

    @GetMapping("/api/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("System is running normally ✅");
    }
}
