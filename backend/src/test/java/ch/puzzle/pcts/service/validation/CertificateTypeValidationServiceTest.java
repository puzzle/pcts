package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateTypeValidationServiceTest
        extends
            ValidationBaseServiceTest<CertificateType, CertificateTypeValidationService> {

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @InjectMocks
    CertificateTypeValidationService service;

    @Override
    CertificateType getValidModel() {
        return new CertificateType(null,
                                   "CertificateType",
                                   BigDecimal.valueOf(10),
                                   "Comment",
                                   Set.of(new Tag(null, "Tag")),
                                   CertificateKind.CERTIFICATE);
    }

    @Override
    CertificateTypeValidationService getService() {
        return service;
    }

    private static CertificateType createCertificateType(String name, BigDecimal points,
                                                         CertificateKind certificateKind) {
        CertificateType c = new CertificateType();
        c.setName(name);
        c.setPoints(points);
        c.setComment("Comment");
        c.setTags(Set.of(new Tag(null, "Tag")));
        c.setCertificateKind(certificateKind);

        return c;
    }

    static Stream<Arguments> invalidModelProvider() {
        BigDecimal validBigDecimal = BigDecimal.valueOf(1);
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments
                        .of(createCertificateType(null, validBigDecimal, CertificateKind.CERTIFICATE),
                            List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificateType("", validBigDecimal, CertificateKind.CERTIFICATE),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificateType("h", validBigDecimal, CertificateKind.CERTIFICATE),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "h"))),
                    Arguments
                            .of(createCertificateType(tooLongName, validBigDecimal, CertificateKind.CERTIFICATE),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    tooLongName))),
                    Arguments
                            .of(createCertificateType("Name", null, CertificateKind.CERTIFICATE),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "points"))),
                    Arguments
                            .of(createCertificateType("Name", BigDecimal.valueOf(-1), CertificateKind.CERTIFICATE),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "points",
                                                    FieldKey.IS,
                                                    "-1"))));
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate kind is not certificate")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateKindIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateCertificateKind(CertificateKind.LEADERSHIP_TRAINING));
        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "certificateKind",
                            FieldKey.IS,
                            "LEADERSHIP_TRAINING",
                            FieldKey.ENTITY,
                            "certificateType")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        CertificateType leadershipExperience = getValidModel();

        when(persistenceService.getByName(leadershipExperience.getName()))
                .thenReturn(Optional.of(new CertificateType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "CertificateType", FieldKey.ENTITY, "certificateType")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        CertificateType certificateType = getValidModel();
        CertificateType newCertificateType = getValidModel();
        certificateType.setId(2L);

        when(persistenceService.getByName(newCertificateType.getName())).thenReturn(Optional.of(certificateType));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, newCertificateType));

        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "CertificateType", FieldKey.ENTITY, "certificateType")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        CertificateType certificateType = getValidModel();

        CertificateTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(certificateType);

        verify(spyService).validateOnCreate(certificateType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        CertificateType certificate = getValidModel();

        CertificateTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, certificate);

        verify(spyService).validateOnUpdate(id, certificate);
        verifyNoMoreInteractions(persistenceService);
    }
}
