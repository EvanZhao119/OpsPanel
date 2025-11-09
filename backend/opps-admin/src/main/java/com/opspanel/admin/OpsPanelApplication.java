package com.opspanel.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.opspanel")
public class OpsPanelApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpsPanelApplication.class, args);
    }
}
