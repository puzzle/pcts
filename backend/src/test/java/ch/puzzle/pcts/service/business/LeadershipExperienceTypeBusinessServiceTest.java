package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.LeadershipTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceTypeBusinessServiceTest
        extends
            BaseBusinessTest<CertificateType, LeadershipTypePersistenceService, LeadershipExperienceTypeValidationService, LeadershipExperienceTypeBusinessService> {

    @Mock
    CertificateType certificateType;

    @Mock
    List<CertificateType> certificateTypes;

    @Mock
    LeadershipTypePersistenceService persistenceService;

    @Mock
    LeadershipExperienceTypeValidationService validationService;

    @InjectMocks
    LeadershipExperienceTypeBusinessService businessService;

    @Override
    CertificateType getModel() {
        return certificateType;
    }

    @Override
    LeadershipTypePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    LeadershipExperienceTypeValidationService getValidationService() {
        return validationService;
    }

    @Override
    LeadershipExperienceTypeBusinessService getBusinessService() {
        return businessService;
    }

    @Override
    void shouldNotDeleteAndThrowException() {
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(certificateTypes);
        when(certificateTypes.size()).thenReturn(2);

        List<CertificateType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(certificateTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<CertificateType> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
