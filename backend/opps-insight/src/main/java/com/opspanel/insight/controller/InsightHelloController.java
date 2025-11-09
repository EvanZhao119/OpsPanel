package com.opspanel.insight.controller;

import com.opspanel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsightHelloController {
    @GetMapping("/api/insight/_hello")
    public ApiResponse<String> hello() {
        return ApiResponse.ok("insight module ok");
    }
}
