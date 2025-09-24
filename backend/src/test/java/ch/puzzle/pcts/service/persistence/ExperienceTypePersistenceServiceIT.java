package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.experienceType.ExperienceType;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
class ExperienceTypePersistenceServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ExperienceTypePersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get experienceType by id")
    @Test
    @Order(1)
    void shouldGetExperienceTypeById() {
        Optional<ExperienceType> experienceType = persistenceService.getById(2L);

        assertThat(experienceType).isPresent();
        assertThat(experienceType.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all experienceTypes")
    @Test
    @Order(1)
    void shouldGetAllExperienceTypes() {
        List<ExperienceType> all = persistenceService.getAll();

        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(ExperienceType::getName)
                .containsExactlyInAnyOrder("ExperienceType 1", "ExperienceType 2");
    }

    @DisplayName("Should create experienceType")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        ExperienceType experienceType = new ExperienceType(null,
                                                           "ExperienceType 3",
                                                           BigDecimal.valueOf(10),
                                                           BigDecimal.valueOf(5),
                                                           BigDecimal.valueOf(2));

        ExperienceType result = persistenceService.create(experienceType);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(experienceType.getName());
        assertThat(result.getHighlyRelevantPoints()).isEqualTo(new BigDecimal("10.00"));
        assertThat(result.getLimitedRelevantPoints()).isEqualTo(new BigDecimal("5.00"));
        assertThat(result.getLittleRelevantPoints()).isEqualTo(new BigDecimal("2.00"));
    }

    @DisplayName("Should correctly round after creating a experienceType")
    @Transactional
    @Test
    @Order(2)
    void shouldCorrectlyRoundAfterCreate() {
        ExperienceType experienceType = new ExperienceType(null,
                                                           "ExperienceType 3",
                                                           BigDecimal.valueOf(10.055),
                                                           BigDecimal.valueOf(5.603),
                                                           BigDecimal.valueOf(2.005));

        ExperienceType result = persistenceService.create(experienceType);

        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getName()).isEqualTo(experienceType.getName());
        assertThat(result.getHighlyRelevantPoints()).isEqualTo(new BigDecimal("10.06"));
        assertThat(result.getLimitedRelevantPoints()).isEqualTo(new BigDecimal("5.60"));
        assertThat(result.getLittleRelevantPoints()).isEqualTo(new BigDecimal("2.01"));
    }

    @DisplayName("Should correctly round after updating a experienceType")
    @Transactional
    @Test
    @Order(2)
    void shouldCorrectlyRoundAfterUpdate() {
        long id = 2;
        ExperienceType updated = new ExperienceType(null,
                                                    "Updated experienceType",
                                                    BigDecimal.valueOf(10.055),
                                                    BigDecimal.valueOf(5.603),
                                                    BigDecimal.valueOf(2.005));

        persistenceService.update(id, updated);
        Optional<ExperienceType> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("Updated experienceType");
        assertThat(result.get().getHighlyRelevantPoints()).isEqualTo(new BigDecimal("10.06"));
        assertThat(result.get().getLimitedRelevantPoints()).isEqualTo(new BigDecimal("5.60"));
        assertThat(result.get().getLittleRelevantPoints()).isEqualTo(new BigDecimal("2.01"));
    }

    @DisplayName("Should update experienceType")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 2;
        ExperienceType updated = new ExperienceType(null,
                                                    "Updated experienceType",
                                                    BigDecimal.valueOf(20),
                                                    BigDecimal.valueOf(10),
                                                    BigDecimal.valueOf(5));

        persistenceService.update(id, updated);
        Optional<ExperienceType> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("Updated experienceType");
        assertThat(result.get().getHighlyRelevantPoints()).isEqualTo(new BigDecimal("20.00"));
        assertThat(result.get().getLimitedRelevantPoints()).isEqualTo(new BigDecimal("10.00"));
        assertThat(result.get().getLittleRelevantPoints()).isEqualTo(new BigDecimal("5.00"));
    }

    @DisplayName("Should delete experienceType")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);

        Optional<ExperienceType> result = persistenceService.getById(id);
        assertThat(result).isEmpty();
    }
}
