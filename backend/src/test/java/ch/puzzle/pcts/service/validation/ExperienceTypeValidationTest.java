package ch.puzzle.pcts.service.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceTypeValidationTest extends ValidationBaseServiceTest<ExperienceType, ExperienceTypeValidationService> {

    @InjectMocks
    ExperienceTypeValidationService service;

    @Mock
    private ExperienceTypePersistenceService persistenceService;

    @Override
    ExperienceType getModel() {
        return new ExperienceType(null,
                                  "Experience Type",
                                  BigDecimal.valueOf(7.5),
                                  BigDecimal.valueOf(4.75),
                                  BigDecimal.valueOf(3));
    }

    @Override
    ExperienceTypeValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNull() {
        ExperienceType experienceType = getModel();
        experienceType.setHighlyRelevantPoints(null);
        experienceType.setLimitedRelevantPoints(null);
        experienceType.setLittleRelevantPoints(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(experienceType));

        assertThat(exception.getReason())
                .contains(List
                        .of("ExperienceType.highlyRelevantPoints must not be null.",
                            "ExperienceType.limitedRelevantPoints must not be null.",
                            "ExperienceType.littleRelevantPoints must not be null."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNegative() {
        ExperienceType experienceType = getModel();
        experienceType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        experienceType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        experienceType.setLittleRelevantPoints(new BigDecimal("-1.0"));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(experienceType));

        assertThat(exception.getReason())
                .contains(List
                        .of("ExperienceType.highlyRelevantPoints must not be negative.",
                            "ExperienceType.limitedRelevantPoints must not be negative.",
                            "ExperienceType.littleRelevantPoints must not be negative."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNull() {
        ExperienceType experienceType = getModel();
        experienceType.setHighlyRelevantPoints(null);
        experienceType.setLimitedRelevantPoints(null);
        experienceType.setLittleRelevantPoints(null);
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, experienceType));

        assertThat(exception.getReason())
                .contains(List
                        .of("ExperienceType.highlyRelevantPoints must not be null.",
                            "ExperienceType.limitedRelevantPoints must not be null.",
                            "ExperienceType.littleRelevantPoints must not be null."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNegative() {
        ExperienceType experienceType = getModel();
        experienceType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        experienceType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        experienceType.setLittleRelevantPoints(new BigDecimal("-1.0"));
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, experienceType));

        assertThat(exception.getReason())
                .contains(List
                        .of("ExperienceType.highlyRelevantPoints must not be negative.",
                            "ExperienceType.limitedRelevantPoints must not be negative.",
                            "ExperienceType.littleRelevantPoints must not be negative."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        ExperienceType experienceType = getModel();

        when(persistenceService.getByName(experienceType.getName())).thenReturn(Optional.of(new ExperienceType()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(experienceType));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        ExperienceType experienceType = getModel();
        ExperienceType newExperienceType = getModel();
        experienceType.setId(2L);

        when(persistenceService.getByName(newExperienceType.getName())).thenReturn(Optional.of(experienceType));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, newExperienceType));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
