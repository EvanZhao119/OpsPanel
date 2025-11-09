package com.opspanel.dashboard.controller;

import com.opspanel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardHelloController {
    @GetMapping("/api/dashboard/_hello")
    public ApiResponse<String> hello() {
        return ApiResponse.ok("dashboard module ok");
    }
}
