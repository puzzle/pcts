package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
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
class LeadershipExperienceTypeValidationServiceTest
        extends
            ValidationBaseServiceTest<CertificateType, LeadershipExperienceTypeValidationService> {

    @InjectMocks
    LeadershipExperienceTypeValidationService service;

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @Override
    CertificateType getValidModel() {
        return new CertificateType(null,
                                   "Leadership Experience Type",
                                   BigDecimal.valueOf(10),
                                   "Comment",
                                   CertificateKind.YOUTH_AND_SPORT);
    }

    @Override
    LeadershipExperienceTypeValidationService getService() {
        return service;
    }

    private static CertificateType createCertificate(String name, BigDecimal points, CertificateKind certificateKind) {
        CertificateType c = new CertificateType();
        c.setName(name);
        c.setPoints(points);
        c.setComment("Comment");
        c.setTags(Set.of(new Tag(null, "Tag")));
        c.setCertificateKind(certificateKind);

        return c;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments
                        .of(createCertificate(null, new BigDecimal(1), CertificateKind.CERTIFICATE),
                            List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificate("", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificate("  ", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificate("S", new BigDecimal(1), CertificateKind.CERTIFICATE),
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
                                                    "S"))),

                    Arguments
                            .of(createCertificate("  S ", new BigDecimal(1), CertificateKind.CERTIFICATE),
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
                                                    "S"))),

                    Arguments
                            .of(createCertificate(tooLongName, new BigDecimal(1), CertificateKind.CERTIFICATE),
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
                            .of(createCertificate("LeadershipExperience", null, CertificateKind.CERTIFICATE),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "points"))),

                    Arguments
                            .of(createCertificate("LeadershipExperience",
                                                  new BigDecimal(-1),
                                                  CertificateKind.CERTIFICATE),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "points",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createCertificate("LeadershipExperience", new BigDecimal(1), null),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "certificateKind"))));
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not leadership experience")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateCertificateKind(CertificateKind.CERTIFICATE));

        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "certificateKind",
                            FieldKey.IS,
                            "CERTIFICATE",
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        CertificateType leadershipExperienceType = getValidModel();

        when(persistenceService.getByName(leadershipExperienceType.getName()))
                .thenReturn(Optional.of(new CertificateType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperienceType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "name",
                            FieldKey.IS,
                            "Leadership Experience Type",
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        CertificateType newLeadershipExperience = getValidModel();
        CertificateType leadershipExperience = getValidModel();
        leadershipExperience.setId(2L);

        when(persistenceService.getByName(newLeadershipExperience.getName()))
                .thenReturn(Optional.of(leadershipExperience));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, newLeadershipExperience));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "name",
                            FieldKey.IS,
                            "Leadership Experience Type",
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        CertificateType leadershipExperienceType = getValidModel();

        LeadershipExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(leadershipExperienceType);

        verify(spyService).validateOnCreate(leadershipExperienceType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        CertificateType leadershipExperience = getValidModel();

        LeadershipExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, leadershipExperience);

        verify(spyService).validateOnUpdate(id, leadershipExperience);
        verifyNoMoreInteractions(persistenceService);
    }
}
