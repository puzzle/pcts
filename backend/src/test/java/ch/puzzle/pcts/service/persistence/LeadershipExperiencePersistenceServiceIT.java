package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
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
class LeadershipExperiencePersistenceServiceIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private LeadershipExperiencePersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get leadershipExperience by id")
    @Test
    @Order(1)
    void shouldGetLeadershipExperienceById() {
        Optional<Certificate> leadershipExperience = persistenceService.getById(7L);

        assertThat(leadershipExperience).isPresent();
        assertThat(leadershipExperience.get().getId()).isEqualTo(7L);
        assertThat(leadershipExperience.get().getName()).isEqualTo("LeadershipExperience 3");
        assertThat(leadershipExperience.get().getPoints()).isEqualTo(new BigDecimal("3.00"));
        assertThat(leadershipExperience.get().getComment()).isEqualTo("This is LeadershipExperience 3");
        assertThat(leadershipExperience.get().getCertificateType()).isEqualTo(CertificateType.LEADERSHIP_TRAINING);

    }

    @DisplayName("Should get all leadershipExperiences")
    @Test
    @Order(1)
    void shouldGetAllLeadershipExperiences() {
        List<Certificate> all = persistenceService.getAll();

        assertThat(all).hasSize(3);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("LeadershipExperience 1",
                                           "LeadershipExperience 2",
                                           "LeadershipExperience 3");
    }

    @DisplayName("Should create leadershipExperience")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        Certificate leadershipExperience = new Certificate(null,
                                                           "Created Leadership Experience",
                                                           BigDecimal.valueOf(3),
                                                           "This is a newly created Leadership Experience",
                                                           CertificateType.LEADERSHIP_TRAINING);

        Certificate result = persistenceService.create(leadershipExperience);

        assertThat(result.getId()).isEqualTo(8L);
        assertThat(result.getName()).isEqualTo(leadershipExperience.getName());
        assertThat(result.getPoints()).isEqualTo(leadershipExperience.getPoints());
        assertThat(result.getComment()).isEqualTo(leadershipExperience.getComment());
        assertThat(result.getCertificateType()).isEqualTo(leadershipExperience.getCertificateType());

    }

    @DisplayName("Should update leadershipExperience")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 4;
        Certificate leadershipExperience = new Certificate(null,
                                                           "Updated leadershipExperience",
                                                           BigDecimal.valueOf(3),
                                                           "This is a updated leadershipExperience",
                                                           CertificateType.MILITARY_FUNCTION);

        persistenceService.update(id, leadershipExperience);
        Optional<Certificate> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(leadershipExperience.getName()).isEqualTo("Updated leadershipExperience");
        assertThat(leadershipExperience.getPoints()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(leadershipExperience.getComment()).isEqualTo("This is a updated leadershipExperience");
        assertThat(leadershipExperience.getCertificateType()).isEqualTo(CertificateType.MILITARY_FUNCTION);

    }

    @DisplayName("Should delete leadershipExperience")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);

        Optional<Certificate> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }
}
