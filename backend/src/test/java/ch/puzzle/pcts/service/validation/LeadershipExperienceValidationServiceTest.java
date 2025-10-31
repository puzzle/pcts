package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
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
class LeadershipExperienceValidationServiceTest
        extends
            ValidationBaseServiceTest<Certificate, LeadershipExperienceValidationService> {

    @InjectMocks
    LeadershipExperienceValidationService service;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Override
    Certificate getModel() {
        return new Certificate(null,
                               "Leadership Experience",
                               BigDecimal.valueOf(10),
                               "Comment",
                               CertificateType.YOUTH_AND_SPORT);
    }

    @Override
    LeadershipExperienceValidationService getService() {
        return service;
    }

    private static Certificate createCertificate(String name, BigDecimal points, CertificateType certificateType) {
        Certificate c = new Certificate();
        c.setName(name);
        c.setPoints(points);
        c.setComment("Comment");
        c.setTags(Set.of(new Tag(null, "Tag")));
        c.setCertificateType(certificateType);

        return c;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments
                        .of(createCertificate(null, new BigDecimal(1), CertificateType.CERTIFICATE),
                            "Certificate.name must not be null."),
                    Arguments
                            .of(createCertificate("", new BigDecimal(1), CertificateType.CERTIFICATE),
                                "Certificate.name must not be blank."),
                    Arguments
                            .of(createCertificate("  ", new BigDecimal(1), CertificateType.CERTIFICATE),
                                "Certificate.name must not be blank."),
                    Arguments
                            .of(createCertificate("S", new BigDecimal(1), CertificateType.CERTIFICATE),
                                "Certificate.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createCertificate("  S ", new BigDecimal(1), CertificateType.CERTIFICATE),
                                "Certificate.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createCertificate(tooLongName, new BigDecimal(1), CertificateType.CERTIFICATE),
                                String
                                        .format("Certificate.name size must be between 2 and 250, given %s.",
                                                tooLongName)),
                    Arguments
                            .of(createCertificate("LeadershipExperience", null, CertificateType.CERTIFICATE),
                                "Certificate.points must not be null."),
                    Arguments
                            .of(createCertificate("LeadershipExperience",
                                                  new BigDecimal(-1),
                                                  CertificateType.CERTIFICATE),
                                "Certificate.points must not be negative."),
                    Arguments
                            .of(createCertificate("LeadershipExperience", new BigDecimal(1), null),
                                "Certificate.certificateType must not be null."));
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not leadership experience")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateCertificateType(CertificateType.CERTIFICATE));

        assertEquals("Certificate.CertificateType is not leadership experience.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Certificate leadershipExperience = getModel();

        when(persistenceService.getByName(leadershipExperience.getName())).thenReturn(Optional.of(new Certificate()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        Certificate newLeadershipExperience = getModel();
        Certificate leadershipExperience = getModel();
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
        Certificate leadershipExperience = getModel();

        LeadershipExperienceValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(leadershipExperience);

        verify(spyService).validateOnCreate(leadershipExperience);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Certificate leadershipExperience = getModel();

        LeadershipExperienceValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, leadershipExperience);

        verify(spyService).validateOnUpdate(id, leadershipExperience);
        verifyNoMoreInteractions(persistenceService);
    }
}
