package com.opspanel.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration for external/public APIs.
 * Keeps public documentation separated from internal admin endpoints.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI publicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpsPanel Public API")
                        .description("Public-facing API documentation for external integrations.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OpsPanel API Team")
                                .email("support@opspanel.com")));
    }
}
