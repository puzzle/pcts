package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Certificate;
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
class CertificatePersistenceServiceIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CertificatePersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get certificate by id")
    @Test
    @Order(1)
    void shouldGetCertificateById() {
        Optional<Certificate> certificate = persistenceService.getById(3L);

        assertThat(certificate).isPresent();
        assertThat(certificate.get().getId()).isEqualTo(3L);
        assertThat(certificate.get().getName()).isEqualTo("Certificate 3");
        assertThat(certificate.get().getPoints()).isEqualTo(new BigDecimal("3.00"));
        assertThat(certificate.get().getComment()).isEqualTo("This is Certificate 3");
    }

    @DisplayName("Should get all certificates")
    @Test
    @Order(1)
    void shouldGetAllCertificates() {
        List<Certificate> all = persistenceService.getAll();

        assertThat(all).hasSize(4);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("Certificate 1", "Certificate 2", "Certificate 3", "Certificate 4");
    }

    @DisplayName("Should create certificate")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        Certificate certificate = new Certificate(null,
                                                  "Created certificate",
                                                  BigDecimal.valueOf(3),
                                                  "This is a newly created certificate");

        Certificate result = persistenceService.create(certificate);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getName()).isEqualTo(certificate.getName());
        assertThat(result.getPoints()).isEqualTo(certificate.getPoints());
        assertThat(result.getComment()).isEqualTo(certificate.getComment());
    }

    @DisplayName("Should update certificate")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 4;
        Certificate certificate = new Certificate(null,
                                                  "Updated certificate",
                                                  BigDecimal.valueOf(3),
                                                  "This is a updated certificate");

        persistenceService.update(id, certificate);
        Optional<Certificate> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(certificate.getName()).isEqualTo("Updated certificate");
        assertThat(certificate.getPoints()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(certificate.getComment()).isEqualTo("This is a updated certificate");
    }

    @DisplayName("Should delete certificate")
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
