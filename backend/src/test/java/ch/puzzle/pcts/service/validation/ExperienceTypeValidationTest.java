package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
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

    private static ExperienceType createExperienceType(String name, BigDecimal highlyRelevantPoints,
                                                       BigDecimal limitedRelevantPoints,
                                                       BigDecimal littleRelevantPoints) {
        ExperienceType e = new ExperienceType();
        e.setName(name);
        e.setHighlyRelevantPoints(highlyRelevantPoints);
        e.setLimitedRelevantPoints(limitedRelevantPoints);
        e.setLittleRelevantPoints(littleRelevantPoints);

        return e;
    }

    static Stream<Arguments> invalidModelProvider() {
        String validName = "Valid Name";
        String tooLongName = new String(new char[251]).replace("\0", "s");
        BigDecimal validBigDecimal = BigDecimal.valueOf(1);
        BigDecimal negativeBigDecimal = BigDecimal.valueOf(-1);

        return Stream
                .of(Arguments
                        .of(createExperienceType(null, validBigDecimal, validBigDecimal, validBigDecimal),
                            "ExperienceType.name must not be null."),
                    Arguments
                            .of(createExperienceType("", validBigDecimal, validBigDecimal, validBigDecimal),
                                "ExperienceType.name must not be blank."),
                    Arguments
                            .of(createExperienceType("h", validBigDecimal, validBigDecimal, validBigDecimal),
                                "ExperienceType.name size must be between 2 and 250, given h."),
                    Arguments
                            .of(createExperienceType(tooLongName, validBigDecimal, validBigDecimal, validBigDecimal),
                                String
                                        .format("ExperienceType.name size must be between 2 and 250, given %s.",
                                                tooLongName)),
                    Arguments
                            .of(createExperienceType(validName, null, validBigDecimal, validBigDecimal),
                                "ExperienceType.highlyRelevantPoints must not be null."),
                    Arguments
                            .of(createExperienceType(validName, negativeBigDecimal, validBigDecimal, validBigDecimal),
                                "ExperienceType.highlyRelevantPoints must not be negative."),
                    Arguments
                            .of(createExperienceType(validName, validBigDecimal, null, validBigDecimal),
                                "ExperienceType.limitedRelevantPoints must not be null."),
                    Arguments
                            .of(createExperienceType(validName, validBigDecimal, negativeBigDecimal, validBigDecimal),
                                "ExperienceType.limitedRelevantPoints must not be negative."),
                    Arguments
                            .of(createExperienceType(validName, validBigDecimal, validBigDecimal, null),
                                "ExperienceType.littleRelevantPoints must not be null."),
                    Arguments
                            .of(createExperienceType(validName, validBigDecimal, validBigDecimal, negativeBigDecimal),
                                "ExperienceType.littleRelevantPoints must not be negative."));
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
