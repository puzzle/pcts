package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.degreetype.DegreeType;
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
class DegreeTypePersistenceServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DegreeTypePersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get degreeTypes by id")
    @Test
    @Order(1)
    void shouldGetDegreeTypeById() {
        Optional<DegreeType> degreeType = persistenceService.getById(2L);

        assertThat(degreeType).isPresent();
        assertThat(degreeType.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all degreeTypes")
    @Test
    @Order(1)
    void shouldGetAllDegreeTypes() {
        List<DegreeType> all = persistenceService.getAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(DegreeType::getName).containsExactlyInAnyOrder("Degree type 1", "Degree type 2");
    }

    @DisplayName("Should create degreeTypes")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        DegreeType degreeType = new DegreeType(null,
                                               "DegreeTypes 3",
                                               BigDecimal.valueOf(10.055),
                                               BigDecimal.valueOf(5.603),
                                               BigDecimal.valueOf(2.005));

        DegreeType result = persistenceService.create(degreeType);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(degreeType.getName());
        assertThat(result.getHighlyRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(10.055));
        assertThat(result.getLimitedRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(5.603));
        assertThat(result.getLittleRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(2.005));
    }

    @DisplayName("Should update degreeType")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        Long id = 2L;
        DegreeType updated = new DegreeType(null,
                                            "Updated degreeType",
                                            BigDecimal.valueOf(10.055),
                                            BigDecimal.valueOf(5.603),
                                            BigDecimal.valueOf(2.005));

        persistenceService.update(id, updated);
        Optional<DegreeType> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("Updated degreeType");
        assertThat(result.get().getHighlyRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(10.055));
        assertThat(result.get().getLimitedRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(5.603));
        assertThat(result.get().getLittleRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(2.005));
    }

    @DisplayName("Should delete degreeType")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<DegreeType> result = persistenceService.getById(id);
        assertThat(result).isEmpty();
    }
}