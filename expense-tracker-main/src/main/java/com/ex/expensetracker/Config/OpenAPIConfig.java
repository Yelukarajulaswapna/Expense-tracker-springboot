package com.ex.expensetracker.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI expenseTrackerOpenAPI() {
        return new OpenAPI().info(new Info().title("New Expense tracker API")
                .description("Expense tracker APi reference").version("2.0"));
    }
}
