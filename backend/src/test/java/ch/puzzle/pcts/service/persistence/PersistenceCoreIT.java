package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.util.IT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
class PersistenceCoreIT {
    @Autowired
    PostgreSQLContainer<?> postgres;

    @DisplayName("Should establish DB connection")
    @Test
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }
}
