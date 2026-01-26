package ch.puzzle.pcts.service.business;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.puzzle.pcts.util.IT;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IT
class CalculationBusinessServiceIT {

    @Autowired
    CalculationBusinessService calcBusinessService;

    @Transactional
    @DisplayName("Should calculate the points of John Doe correctly")
    @Test
    @Sql({ "/db/test-data-migration/db-scripts/alter__sequences.sql",
            "/db/test-data-migration/db-scripts/insert__john_doe.sql" })
    void shouldCalculateJohnDoe() {
        assertThat(calcBusinessService.getById(1000L).getPoints().setScale(1, RoundingMode.FLOOR))
                .isEqualTo(BigDecimal.valueOf(15.7));
    }

    @Transactional
    @DisplayName("Should calculate the points of Hermine Sidney correctly")
    @Test
    @Sql({ "/db/test-data-migration/db-scripts/alter__sequences.sql",
            "/db/test-data-migration/db-scripts/insert__hermine_sidney.sql" })
    void shouldCalculateHermineSidney() {
        assertThat(calcBusinessService.getById(1000L).getPoints().setScale(1, RoundingMode.FLOOR))
                .isEqualTo(BigDecimal.valueOf(95.7));
    }
}
