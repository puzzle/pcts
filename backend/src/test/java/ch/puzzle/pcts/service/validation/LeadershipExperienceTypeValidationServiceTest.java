package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import java.math.BigDecimal;
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
                            "CertificateType.name must not be null."),
                    Arguments
                            .of(createCertificate("", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                "CertificateType.name must not be blank."),
                    Arguments
                            .of(createCertificate("  ", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                "CertificateType.name must not be blank."),
                    Arguments
                            .of(createCertificate("S", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                "CertificateType.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createCertificate("  S ", new BigDecimal(1), CertificateKind.CERTIFICATE),
                                "CertificateType.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createCertificate(tooLongName, new BigDecimal(1), CertificateKind.CERTIFICATE),
                                String
                                        .format("CertificateType.name size must be between 2 and 250, given %s.",
                                                tooLongName)),
                    Arguments
                            .of(createCertificate("LeadershipExperience", null, CertificateKind.CERTIFICATE),
                                "CertificateType.points must not be null."),
                    Arguments
                            .of(createCertificate("LeadershipExperience",
                                                  new BigDecimal(-1),
                                                  CertificateKind.CERTIFICATE),
                                "CertificateType.points must not be negative."),
                    Arguments
                            .of(createCertificate("LeadershipExperience", new BigDecimal(1), null),
                                "CertificateType.certificateKind must not be null."));
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not leadership experience")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateCertificateKind(CertificateKind.CERTIFICATE));

        assertEquals("CertificateType.certificateKind is not leadership experience.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        CertificateType leadershipExperienceType = getValidModel();

        when(persistenceService.getByName(leadershipExperienceType.getName()))
                .thenReturn(Optional.of(new CertificateType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperienceType));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
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

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
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
