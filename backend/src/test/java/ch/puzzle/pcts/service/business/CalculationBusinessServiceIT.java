package ch.puzzle.pcts.service.business;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.puzzle.pcts.util.IT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IT
class CalculationBusinessServiceIT {

    @Autowired
    CalculationBusinessService calcBusinessService;

    @Transactional
    @DisplayName("Testing the sql script annotation")
    @Test
    @Sql("/db/test-data-migration/db-scripts/test_script.sql")
    void testSqlScript() {

        assertThat(calcBusinessService.getById(1000L).getPoints()).isEqualTo(15.7);
    }
}
