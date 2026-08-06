package ch.puzzle.pctsmigration.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    // Spring Boot findet dieses Bean automatisch und registriert es
    // in seinem globalen ObjectMapper. Dadurch versteht das gesamte
    // Projekt (inklusive Spring AI) sofort JsonNullable!
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }
}
