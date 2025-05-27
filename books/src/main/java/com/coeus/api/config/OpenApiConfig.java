package com.coeus.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This is a class for managing Spring Doc (Swagger) for documenting the API

// Tells Spring this is a class that contains important configuration settings
@Configuration
public class OpenApiConfig {

    // Object managed and instantiated by Spring Boot
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Coeus RESTful API")
                    .version("v1")
                    .termsOfService("https://github.com/vilasbtw/coeus")
                )
        ;
    }

}