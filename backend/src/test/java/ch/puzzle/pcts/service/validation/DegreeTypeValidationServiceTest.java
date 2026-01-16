package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.DEGREE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
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
class DegreeTypeValidationServiceTest extends ValidationBaseServiceTest<DegreeType, DegreeTypeValidationService> {

    @InjectMocks
    DegreeTypeValidationService service;

    @Mock
    private DegreeTypePersistenceService persistenceService;

    @Override
    DegreeType getValidModel() {
        return new DegreeType(null,
                              "Degree Type",
                              BigDecimal.valueOf(7.5),
                              BigDecimal.valueOf(4.75),
                              BigDecimal.valueOf(3));
    }
    @Override
    DegreeTypeValidationService getService() {
        return service;
    }

    private static DegreeType createDegreeType(String name, BigDecimal highlyRelevantPoints,
                                               BigDecimal limitedRelevantPoints, BigDecimal littleRelevantPoints) {
        DegreeType d = new DegreeType();
        d.setName(name);
        d.setHighlyRelevantPoints(highlyRelevantPoints);
        d.setLimitedRelevantPoints(limitedRelevantPoints);
        d.setLittleRelevantPoints(littleRelevantPoints);

        return d;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createDegreeType(null, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                            List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegreeType("", POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegreeType("h", POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "h"))),
                    Arguments
                            .of(createDegreeType(TOO_LONG_STRING,
                                                 POSITIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createDegreeType(VALID_STRING, null, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "highlyRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(VALID_STRING,
                                                 NEGATIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "highlyRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createDegreeType(VALID_STRING, POSITIVE_BIG_DECIMAL, null, POSITIVE_BIG_DECIMAL),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "limitedRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(VALID_STRING,
                                                 POSITIVE_BIG_DECIMAL,
                                                 NEGATIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "limitedRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createDegreeType(VALID_STRING, POSITIVE_BIG_DECIMAL, POSITIVE_BIG_DECIMAL, null),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "littleRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(VALID_STRING,
                                                 POSITIVE_BIG_DECIMAL,
                                                 POSITIVE_BIG_DECIMAL,
                                                 NEGATIVE_BIG_DECIMAL),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "littleRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))));
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        DegreeType degreeType = getValidModel();

        when(persistenceService.getByName(degreeType.getName())).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Degree Type", FieldKey.ENTITY, DEGREE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        DegreeType degreeType = getValidModel();
        DegreeType newDegreeType = getValidModel();
        degreeType.setId(DEGREE_2_ID);

        when(persistenceService.getByName(newDegreeType.getName())).thenReturn(Optional.of(degreeType));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(DEGREE_1_ID, newDegreeType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Degree Type", FieldKey.ENTITY, DEGREE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        DegreeType degreeType = getValidModel();

        DegreeTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<DegreeType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(degreeType);

        verify(spyService).validateOnCreate(degreeType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        DegreeType degreeType = getValidModel();

        DegreeTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<DegreeType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(DEGREE_1_ID, degreeType);

        verify(spyService).validateOnUpdate(DEGREE_1_ID, degreeType);
        verifyNoMoreInteractions(persistenceService);
    }
}