package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.util.TestData.CERTIFICATE_1_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
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
        when(persistenceService.findLeadershipExperience(CERTIFICATE_1_ID)).thenReturn(certificate);

        Certificate result = businessService.getById(CERTIFICATE_1_ID);

        assertEquals(certificate, result);

        verify(validationService).validateOnGetById(CERTIFICATE_1_ID);
        verify(persistenceService).findLeadershipExperience(CERTIFICATE_1_ID);
    }

    @Override
    @DisplayName("Should update certificate")
    @Test
    void shouldUpdate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.update(CERTIFICATE_1_ID, certificate);

        assertEquals(certificate, result);

        verify(certificate).setId(CERTIFICATE_1_ID);
        verify(validationService).validateOnUpdate(CERTIFICATE_1_ID, certificate);
        verify(persistenceService).save(certificate);
    }

    @Override
    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        businessService.delete(CERTIFICATE_1_ID);

        verify(validationService).validateOnDelete(CERTIFICATE_1_ID);
        verify(persistenceService).delete(CERTIFICATE_1_ID);
    }
}
