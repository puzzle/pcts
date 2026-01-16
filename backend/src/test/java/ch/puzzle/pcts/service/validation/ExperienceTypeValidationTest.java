package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    ExperienceType getValidModel() {
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
        return Stream
                .of(Arguments
                        .of(createExperienceType(null, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                            List.of(Map.of(FieldKey.CLASS, "ExperienceType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createExperienceType("", POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createExperienceType("h", POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "h"))),
                    Arguments
                            .of(createExperienceType(TOO_LONG_STRING,
                                            POSITIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createExperienceType(VALID_STRING, null, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "highlyRelevantPoints"))),
                    Arguments
                            .of(createExperienceType(VALID_STRING,
                                                     NEGATIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "highlyRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createExperienceType(VALID_STRING, POSITIVE_BIG_DECIMAL, null, POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "limitedRelevantPoints"))),
                    Arguments
                            .of(createExperienceType(VALID_STRING,
                                            POSITIVE_BIG_DECIMAL,
                                                     NEGATIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "limitedRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createExperienceType(VALID_STRING, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, null),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "littleRelevantPoints"))),
                    Arguments
                            .of(createExperienceType(VALID_STRING,
                                            POSITIVE_BIG_DECIMAL,
                                            POSITIVE_BIG_DECIMAL,
                                                     NEGATIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ExperienceType",
                                                    FieldKey.FIELD,
                                                    "littleRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))));
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        ExperienceType experienceType = getValidModel();

        when(persistenceService.getByName(experienceType.getName())).thenReturn(Optional.of(new ExperienceType()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(experienceType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Experience Type", FieldKey.ENTITY, EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        ExperienceType experienceType = getValidModel();
        ExperienceType newExperienceType = getValidModel();
        experienceType.setId(EXP_TYPE_2_ID);

        when(persistenceService.getByName(newExperienceType.getName())).thenReturn(Optional.of(experienceType));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(EXP_TYPE_1_ID, newExperienceType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Experience Type", FieldKey.ENTITY, EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        ExperienceType experienceType = getValidModel();

        ExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<ExperienceType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(experienceType);

        verify(spyService).validateOnCreate(experienceType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        ExperienceType experienceType = getValidModel();

        ExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<ExperienceType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(EXP_TYPE_1_ID, experienceType);

        verify(spyService).validateOnUpdate(EXP_TYPE_1_ID, experienceType);
        verifyNoMoreInteractions(persistenceService);
    }
}
