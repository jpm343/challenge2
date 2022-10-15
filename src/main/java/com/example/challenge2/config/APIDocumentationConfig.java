package com.example.challenge2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIDocumentationConfig {
  @Bean
  public OpenAPI openApiConfig() {
    return new OpenAPI()
        .info(
            new Info()
                .title("IONIX challenge API")
                .description("challenge for IONIX application")
                .version("v0.0.1"));
  }
}
