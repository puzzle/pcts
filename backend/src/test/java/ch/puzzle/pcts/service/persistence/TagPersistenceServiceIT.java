package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Tag;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TagPersistenceServiceIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TagPersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should create tag")
    @Transactional
    @Test
    @Order(1)
    void shouldCreate() {
        Tag tag = new Tag(null, "Very important tag");

        Tag result = persistenceService.create(tag);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(tag.getName());
    }

    @DisplayName("Should find tag by name written in different cases")
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = { "Longer tag name", "longer tag name", "LONGER TAG NAME", "loNGER tAg NAme" })
    @Order(2)
    void shouldFindTagWithDifferentCases(String tagName) {
        Tag result = persistenceService.findWithIgnoreCase(tagName).get();

        assertThat(result.getName()).isEqualTo("Longer tag name");
        assertThat(result.getId()).isEqualTo(2L);
    }
}
