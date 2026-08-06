package ch.puzzle.pcts;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerName = "OIDC Token";
        final String apiKeyName = "API Key";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(bearerName))
                .addSecurityItem(new SecurityRequirement().addList(apiKeyName))
                .components(new Components()
                        .addSecuritySchemes(bearerName,
                                            new SecurityScheme()
                                                    .name(bearerName)
                                                    .type(SecurityScheme.Type.HTTP)
                                                    .scheme("bearer")
                                                    .bearerFormat("JWT"))
                        .addSecuritySchemes(apiKeyName,
                                            new SecurityScheme()
                                                    .name(apiKeyName)
                                                    .type(SecurityScheme.Type.APIKEY)
                                                    .in(SecurityScheme.In.HEADER)
                                                    .name("X-API-Key")))
                .info(new Info().title("PCTS API"));
    }
}
