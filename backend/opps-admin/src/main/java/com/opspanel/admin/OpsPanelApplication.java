package com.opspanel.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for OpsPanel backend system.
 */
@SpringBootApplication(scanBasePackages = "com.opspanel")
@MapperScan("com.opspanel.system.mapper")
public class OpsPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsPanelApplication.class, args);
    }
}
