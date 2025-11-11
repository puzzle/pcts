package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import java.util.Optional;
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
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));
        when(certificate.getCertificateKind()).thenReturn(CertificateKind.YOUTH_AND_SPORT);

        CertificateType result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
        verify(validationService).validateCertificateKind(certificate.getCertificateKind());
    }

    @DisplayName("Should throw error when leadership experience type with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals("LeadershipExperience type with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
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

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
