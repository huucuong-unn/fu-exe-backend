package com.exe01.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Value;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    private final String LOCALHOST = "http://localhost:8086/";

    private final String DEPLOYHOST = "https://tortee-463vt.ondigitalocean.app/";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("API-service document")
                .version("v1.0.0").description("Description")
                .license(new License().name("Api License").url("http://domain.vn/license")))
                .servers(List.of(new Server().url(DEPLOYHOST).description("Tortee Server")))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service")
                .packagesToScan("com.exe01.backend.controller")
                .build();
    }
}
