package com.example.myidejava.core.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    // todo : constants refactoring
    private static final String AUTHORIZE_NAME = "JWT Token";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v0.0.1")
                .title("MY-IDE-JAVA OpenAPI");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(AUTHORIZE_NAME);
        Components components = new Components()
                .addSecuritySchemes(AUTHORIZE_NAME, new SecurityScheme()
                        .name(AUTHORIZE_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
