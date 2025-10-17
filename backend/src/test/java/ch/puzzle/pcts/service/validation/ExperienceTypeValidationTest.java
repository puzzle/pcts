package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.math.BigDecimal;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceTypeValidationTest extends ValidationBaseServiceTest<ExperienceType, ExperienceTypeValidationService> {

    @InjectMocks
    ExperienceTypeValidationService service;

    @Override
    ExperienceType getModel() {
        return new ExperienceType(null,
                                  "Experience Type",
                                  BigDecimal.valueOf(7.5),
                                  BigDecimal.valueOf(4.75),
                                  BigDecimal.valueOf(3));
    }

    @Override
    void validate() {

    }

    @Override
    ExperienceTypeValidationService getService() {
        return service;
    }
}
