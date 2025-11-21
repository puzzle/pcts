package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
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
        String validName = "Valid Name";
        String tooLongName = new String(new char[251]).replace("\0", "s");
        BigDecimal validBigDecimal = BigDecimal.valueOf(1);
        BigDecimal negativeBigDecimal = BigDecimal.valueOf(-1);

        return Stream
                .of(Arguments
                        .of(createDegreeType(null, validBigDecimal, validBigDecimal, validBigDecimal),
                            List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegreeType("", validBigDecimal, validBigDecimal, validBigDecimal),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegreeType("h", validBigDecimal, validBigDecimal, validBigDecimal),
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
                            .of(createDegreeType(tooLongName, validBigDecimal, validBigDecimal, validBigDecimal),
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
                                                    tooLongName))),
                    Arguments
                            .of(createDegreeType(validName, null, validBigDecimal, validBigDecimal),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "highlyRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(validName, negativeBigDecimal, validBigDecimal, validBigDecimal),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "highlyRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createDegreeType(validName, validBigDecimal, null, validBigDecimal),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "limitedRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(validName, validBigDecimal, negativeBigDecimal, validBigDecimal),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeType",
                                                    FieldKey.FIELD,
                                                    "limitedRelevantPoints",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createDegreeType(validName, validBigDecimal, validBigDecimal, null),
                                List.of(Map.of(FieldKey.CLASS, "DegreeType", FieldKey.FIELD, "littleRelevantPoints"))),
                    Arguments
                            .of(createDegreeType(validName, validBigDecimal, validBigDecimal, negativeBigDecimal),
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
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Degree Type", FieldKey.ENTITY, "degreeType")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        DegreeType degreeType = getValidModel();
        DegreeType newDegreeType = getValidModel();
        degreeType.setId(2L);

        when(persistenceService.getByName(newDegreeType.getName())).thenReturn(Optional.of(degreeType));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newDegreeType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Degree Type", FieldKey.ENTITY, "degreeType")),
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
        Long id = 1L;
        DegreeType degreeType = getValidModel();

        DegreeTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<DegreeType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, degreeType);

        verify(spyService).validateOnUpdate(id, degreeType);
        verifyNoMoreInteractions(persistenceService);
    }
}