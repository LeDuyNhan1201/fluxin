package org.neb.fluxin.infrastructure;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
            .group(apiDocs)
            .packagesToScan("org.neb.fluxin.api")
            .build();
    }

    @Bean
    public OpenAPI openAPI(
        @Value("${openapi.service.title}") String title,
        @Value("${openapi.service.description}") String description,
        @Value("${openapi.service.version}") String version,
        @Value("${openapi.service.server-url}") String serverUrl,
        @Value("${openapi.service.server-description}") String serverDescription
    ) {
        return createOpenAPI(
            title,
            description,
            version,
            serverUrl,
            serverDescription
        );
    }

    private OpenAPI createOpenAPI(
            String title,
            String description,
            String version,
            String serverUrl,
            String serverDescription
    ) {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(serverUrl).description(serverDescription)
                ))
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .email("benlun99999@gmail.com")
                                .name("Ben's side projects")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Find out more about this service")
                        .url("http://abc.com")
                )
                .security(List.of(
                        new SecurityRequirement()
                                .addList("basic_bearer")
                                .addList("oauth2_bearer")))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "basic_bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                                .addSecuritySchemes(
                                        "oauth2_bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .flows(new OAuthFlows().authorizationCode(
                                                        new OAuthFlow()
                                                                .authorizationUrl("${springdoc.oauthflow.authorization-url}")
                                                                .tokenUrl("${springdoc.oauthflow.token-url}")
                                                                .scopes(new Scopes().addString("openid", "openid"))))
                                ));
    }

}
