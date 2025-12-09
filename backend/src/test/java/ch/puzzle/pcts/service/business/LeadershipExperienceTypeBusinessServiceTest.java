package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceTypeBusinessServiceTest {

    @Mock
    private LeadershipExperienceTypeValidationService validationService;

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @Mock
    private CertificateType certificate;

    @Mock
    private List<CertificateType> certificates;

    @InjectMocks
    private LeadershipExperienceTypeBusinessService businessService;

    @DisplayName("Should get leadershipExperience by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        Long id = 1L;
        when(persistenceService.getLeadershipExperienceType(id)).thenReturn(certificate);

        CertificateType result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getLeadershipExperienceType(id);
    }

    @DisplayName("Should create leadershipExperience type")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        CertificateType result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should get all leadershipExperience type")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAllLeadershipExperienceTypes()).thenReturn(certificates);
        when(certificates.size()).thenReturn(2);

        List<CertificateType> result = businessService.getAll();

        assertEquals(certificates, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAllLeadershipExperienceTypes();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should update leadershipExperience type")
    @Test
    void shouldUpdate() {
        Long id = 1L;

        when(persistenceService.save(certificate)).thenReturn(certificate);
        when(persistenceService.getLeadershipExperienceType(id)).thenReturn(certificate);

        CertificateType result = businessService.update(id, certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnUpdate(id, certificate);
        verify(certificate).setId(id);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should delete leadershipExperience type")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getLeadershipExperienceType(id)).thenReturn(certificate);

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
