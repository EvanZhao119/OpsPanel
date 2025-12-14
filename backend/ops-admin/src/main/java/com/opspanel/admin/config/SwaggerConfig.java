package com.opspanel.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration for internal Admin APIs.
 * Provides OpenAPI documentation for system management modules.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI adminOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpsPanel Admin API")
                        .description("Internal management APIs for users, roles, and menus.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OpsPanel Admin Team")
                                .email("admin@opspanel.com")));
    }
}
