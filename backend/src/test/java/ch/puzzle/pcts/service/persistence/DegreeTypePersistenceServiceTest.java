package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeNameDto;
import ch.puzzle.pcts.model.degreeType.DegreeType;
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
public class DegreeTypePersistenceServiceTest {

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

    @DisplayName("Should get degree type by id")
    @Test
    @Order(1)
    void shouldGetDegreeTypeById() {
        Optional<DegreeType> degreeType = persistenceService.getById(1L);

        assertThat(degreeType.isPresent()).isTrue();
        assertThat(degreeType.get().getDegreeTypeId()).isEqualTo(1L);
    }

    @DisplayName("Should get all degree types")
    @Test
    @Order(1)
    void shouldGetAllDegreeTypes() {
        List<DegreeType> all = persistenceService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(DegreeType::getDegreeTypeId).containsExactlyInAnyOrder(1L);
    }

    @DisplayName("Should get all degree types names")
    @Test
    @Order(1)
    void shouldGetAllDegreeTypeNames() {
        List<DegreeTypeNameDto> all = persistenceService.getAllNames();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(DegreeTypeNameDto::name).containsExactlyInAnyOrder("Degree type 1");
    }

    @DisplayName("Should create degree type")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        DegreeType degreeType = new DegreeType(null,
                                               "Degree type 1",
                                               new BigDecimal("1.0"),
                                               new BigDecimal("2.0"),
                                               new BigDecimal("3.0"));

        DegreeType result = persistenceService.create(degreeType);

        assertThat(result.getDegreeTypeId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo(degreeType.getName());
        assertThat(result.getHighlyRelevantPoints()).isEqualTo(degreeType.getHighlyRelevantPoints());
        assertThat(result.getLimitedRelevantPoints()).isEqualTo(degreeType.getLimitedRelevantPoints());
        assertThat(result.getLittleRelevantPoints()).isEqualTo(degreeType.getLittleRelevantPoints());
    }

    @DisplayName("Should update degree type")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 1;
        DegreeType degreeType = new DegreeType(null,
                                               "Updated degree type",
                                               new BigDecimal("2.0"),
                                               new BigDecimal("3.0"),
                                               new BigDecimal("4.0"));

        persistenceService.update(id, degreeType);
        Optional<DegreeType> result = persistenceService.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getDegreeTypeId()).isEqualTo(id);
        assertThat(degreeType.getName()).isEqualTo("Updated degree type");
        assertThat(degreeType.getHighlyRelevantPoints()).isEqualTo(new BigDecimal("2.0"));
        assertThat(degreeType.getLimitedRelevantPoints()).isEqualTo(new BigDecimal("3.0"));
        assertThat(degreeType.getLittleRelevantPoints()).isEqualTo(new BigDecimal("4.0"));
    }

    @DisplayName("Should delete degree type")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);

        Optional<DegreeType> result = persistenceService.getById(id);
        assertThat(result.isPresent()).isFalse();
    }
}
