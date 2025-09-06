package com.alphano.alphano.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
            title = "알파노 API 명세서"
    ),
    security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Authorization")
)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Components components = new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization"));

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(components);
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("Authorization");
    }
}
