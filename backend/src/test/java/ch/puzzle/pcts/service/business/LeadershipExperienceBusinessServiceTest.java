package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceBusinessServiceTest
        extends
            BaseBusinessTest<Certificate, CertificatePersistenceService, LeadershipExperienceValidationService, LeadershipExperienceBusinessService> {

    private static final Long ID = 1L;

    @Mock
    private Certificate certificate;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private LeadershipExperienceValidationService validationService;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @Override
    Certificate getModel() {
        return certificate;
    }

    @Override
    CertificatePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    LeadershipExperienceValidationService getValidationService() {
        return validationService;
    }

    @Override
    LeadershipExperienceBusinessService getBusinessService() {
        return businessService;
    }

    @Override
    @DisplayName("Should get certificate by id")
    @Test
    void shouldGetById() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.getById(ID);

        assertEquals(certificate, result);

        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).findLeadershipExperience(ID);
    }

    @Override
    @DisplayName("Should throw exception when getting a non-existing certificate")
    @Test
    void shouldNotGetByIdAndThrowException() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.getById(ID));

        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).findLeadershipExperience(ID);
    }

    @Override
    @DisplayName("Should update certificate")
    @Test
    void shouldUpdate() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.of(certificate));
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.update(ID, certificate);

        assertEquals(certificate, result);

        verify(certificate).setId(ID);
        verify(validationService).validateOnUpdate(ID, certificate);
        verify(persistenceService).save(certificate);
    }

    @Override
    @DisplayName("Should throw exception when updating a non-existing certificate")
    @Test
    void shouldNotUpdateAndThrowException() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(ID, certificate));

        verify(persistenceService).findLeadershipExperience(ID);
        verify(persistenceService, never()).save(any());
    }

    @Override
    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.of(certificate));

        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }

    @Override
    @DisplayName("Should throw exception when deleting a non-existing certificate")
    @Test
    void shouldNotDeleteAndThrowException() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(ID));

        verify(persistenceService).findLeadershipExperience(ID);
        verify(persistenceService, never()).delete(ID);
    }
}
