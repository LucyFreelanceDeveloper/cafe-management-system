package com.example.cafe;

import com.example.cafe.config.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class CaffeManagementSystemApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CaffeManagementSystemApplication.class, args);
    }

}
