package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceBusinessServiceTest {

    @Mock
    private LeadershipExperienceValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private Certificate certificate;

    @Mock
    private List<Certificate> certificates;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @DisplayName("Should get leadershipExperience by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));
        when(certificate.getCertificateType()).thenReturn(CertificateType.YOUTH_AND_SPORT);

        Certificate result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
        verify(validationService).validateCertificateType(certificate.getCertificateType());
    }

    @DisplayName("Should create leadershipExperience")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should get all leadershipExperiences")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAllLeadershipExperiences()).thenReturn(certificates);
        when(certificates.size()).thenReturn(2);

        List<Certificate> result = businessService.getAll();

        assertEquals(certificates, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAllLeadershipExperiences();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should update leadershipExperiences")
    @Test
    void shouldUpdate() {
        Long id = 1L;

        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.update(id, certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnUpdate(id, certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should delete leadershipExperience")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
