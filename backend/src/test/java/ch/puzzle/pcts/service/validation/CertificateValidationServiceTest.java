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
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {

    @Mock
    private CertificatePersistenceService persistenceService;

    @InjectMocks
    private CertificateValidationService service;

    @Override
    Certificate getValidModel() {
        return new Certificate(null,
                               "Certificate",
                               BigDecimal.valueOf(10),
                               "Comment",
                               Set.of(new Tag(null, "Tag")),
                               CertificateType.CERTIFICATE);
    }

    @Override
    CertificateValidationService getService() {
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
        BigDecimal validBigDecimal = BigDecimal.valueOf(1);
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments
                        .of(createCertificate(null, validBigDecimal, CertificateType.CERTIFICATE),
                            "Certificate.name must not be null."),
                    Arguments
                            .of(createCertificate("", validBigDecimal, CertificateType.CERTIFICATE),
                                "Certificate.name must not be blank."),
                    Arguments
                            .of(createCertificate("h", validBigDecimal, CertificateType.CERTIFICATE),
                                "Certificate.name size must be between 2 and 250, given h."),
                    Arguments
                            .of(createCertificate(tooLongName, validBigDecimal, CertificateType.CERTIFICATE),
                                String
                                        .format("Certificate.name size must be between 2 and 250, given %s.",
                                                tooLongName)),
                    Arguments
                            .of(createCertificate("Name", null, CertificateType.CERTIFICATE),
                                "Certificate.points must not be null."),
                    Arguments
                            .of(createCertificate("Name", BigDecimal.valueOf(-1), CertificateType.CERTIFICATE),
                                "Certificate.points must not be negative."));
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not certificate")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateCertificateType(CertificateType.LEADERSHIP_TRAINING));

        assertEquals("Certificate.CertificateType is not certificate.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Certificate leadershipExperience = getValidModel();

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
        Certificate certificate = getValidModel();
        Certificate newCertificate = getValidModel();
        certificate.setId(2L);

        when(persistenceService.getByName(newCertificate.getName())).thenReturn(Optional.of(certificate));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newCertificate));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Certificate certificate = getValidModel();

        CertificateValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(certificate);

        verify(spyService).validateOnCreate(certificate);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Certificate certificate = getValidModel();

        CertificateValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, certificate);

        verify(spyService).validateOnUpdate(id, certificate);
        verifyNoMoreInteractions(persistenceService);
    }
}
