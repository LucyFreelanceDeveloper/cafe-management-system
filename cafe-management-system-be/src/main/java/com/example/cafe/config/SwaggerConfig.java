package com.example.cafe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI eCertOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("cafe-management-system-be")
                        .description("Service provide backend implementation of application - connection to database, REST API and JWT authentication.")
                        .version("v1.0.0"));
    }
}