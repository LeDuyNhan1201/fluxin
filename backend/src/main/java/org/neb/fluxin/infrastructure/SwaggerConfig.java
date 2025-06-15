package org.neb.fluxin.infrastructure;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "${openapi.service.title}",
//                description = "${openapi.service.description}",
//                version = "${openapi.service.version}",
//                termsOfService = "${openapi.service.terms-of-service}",
//                license = @License(
//                        name = "${openapi.service.license.name}",
//                        url = "${openapi.service.license.url}"
//                )
//        ),
//        security = {
//                @SecurityRequirement(name = "basic_bearer"),
//                @SecurityRequirement(name = "oauth2_bearer")
//        },
//        servers = {
//            @Server(url = "${openapi.service.server-url}",
//                description = "${openapi.service.server-description}")
//            },
//        externalDocs = @ExternalDocumentation(
//                url = "${openapi.service.external-docs-url}",
//                description = "${openapi.service.external-docs-description}"
//            ))
//@SecurityScheme(name = "basic_bearer", type = SecuritySchemeType.HTTP,
//        scheme = "bearer", bearerFormat = "JWT")
//@SecurityScheme(name = "oauth2_bearer", type = SecuritySchemeType.OAUTH2,
//        flows = @OAuthFlows(
//                authorizationCode = @OAuthFlow(
//                        authorizationUrl = "${springdoc.oauthflow.authorization-url}",
//                        tokenUrl = "${springdoc.oauthflow.token-url}",
//                        scopes = {
//                                @OAuthScope(name = "openid", description = "openid")
//                        })))
public class SwaggerConfig {

}