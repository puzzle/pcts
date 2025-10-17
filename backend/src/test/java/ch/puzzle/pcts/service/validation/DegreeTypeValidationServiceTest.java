package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import java.math.BigDecimal;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeTypeValidationServiceTest extends ValidationBaseServiceTest<DegreeType, DegreeTypeValidationService> {

    @InjectMocks
    DegreeTypeValidationService service;

    @Override
    DegreeType getModel() {
        return new DegreeType(null,
                              "Degree Type",
                              BigDecimal.valueOf(7.5),
                              BigDecimal.valueOf(4.75),
                              BigDecimal.valueOf(3));
    }

    @Override
    void validate() {

    }

    @Override
    DegreeTypeValidationService getService() {
        return service;
    }
}