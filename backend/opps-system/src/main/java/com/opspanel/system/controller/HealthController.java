package com.opspanel.system.controller;

import com.opspanel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("OpsPanel backend is up");
    }
}
