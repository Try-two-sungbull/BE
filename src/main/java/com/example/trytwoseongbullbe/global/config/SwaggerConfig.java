package com.example.trytwoseongbullbe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI(Environment env) {
        String serverUrl = resolveServerUrl(env);

        return new OpenAPI()
                .info(new Info()
                        .title("Try-two Seongbull API")
                        .description("공고문 자동 생성 해커톤 백엔드 API 문서")
                        .version("v1"))
                .addServersItem(new Server()
                        .url(serverUrl)
                        .description("local".equalsIgnoreCase(getActiveProfile(env)) ? "Local" : "Production"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(
                        BEARER_SCHEME_NAME,
                        new SecurityScheme()
                                .name(BEARER_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }

    private String resolveServerUrl(Environment env) {
        String active = getActiveProfile(env);
        if ("local".equalsIgnoreCase(active)) {
            return "http://localhost:8080";
        }
        return "https://be.hack.bluerack.org";
    }

    private String getActiveProfile(Environment env) {
        String[] profiles = env.getActiveProfiles();
        return profiles.length > 0 ? profiles[0] : "";
    }
}