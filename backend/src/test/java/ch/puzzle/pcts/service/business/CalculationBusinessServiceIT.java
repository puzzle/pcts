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
    @DisplayName("Should calculate the points for a simple member (John Doe) correctly")
    @Test
    @Sql({ "/db/test-data-migration/db-scripts/alter__sequences.sql",
            "/db/test-data-migration/db-scripts/insert__john_doe.sql" })
    void shouldCalculateSimpleMemberCorrectly() {
        assertThat(calcBusinessService.getById(1000L).getPoints().setScale(2, RoundingMode.CEILING))
                .isEqualByComparingTo(BigDecimal.valueOf(15.80));
    }

    @Transactional
    @DisplayName("Should calculate the points for a complex member (Hermine Sidney) correctly")
    @Test
    @Sql({ "/db/test-data-migration/db-scripts/alter__sequences.sql",
            "/db/test-data-migration/db-scripts/insert__hermine_sidney.sql" })
    void shouldCalculateComplexMemberCorrectly() {
        assertThat(calcBusinessService.getById(1000L).getPoints().setScale(2, RoundingMode.CEILING))
                .isEqualTo(BigDecimal.valueOf(95.76));
    }

    @Transactional
    @DisplayName("Should calculate the points for a complex multi-role member (Devon Ricky) correctly")
    @Test
    @Sql({ "/db/test-data-migration/db-scripts/alter__sequences.sql",
            "/db/test-data-migration/db-scripts/insert__devon_ricky.sql" })
    void shouldCalculateComplexMultiRoleMemberCorrectly() {
        assertThat(calcBusinessService.getById(1000L).getPoints().setScale(2, RoundingMode.CEILING))
                .isEqualTo(BigDecimal.valueOf(87.51));

        assertThat(calcBusinessService.getById(1001L).getPoints().setScale(2, RoundingMode.CEILING))
                .isEqualTo(BigDecimal.valueOf(84.48));
    }
}
