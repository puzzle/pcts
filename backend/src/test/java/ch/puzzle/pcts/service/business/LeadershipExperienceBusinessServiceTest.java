package ch.puzzle.pcts.service.business;

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
class LeadershipExperienceBusinessServiceTest {
    private static final Long ID = 1L;

    @Mock
    private LeadershipExperienceValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private Certificate leadershipExperience;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @DisplayName("Should get leadership experience by id")
    @Test
    void shouldGetLeadershipExperienceById() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(leadershipExperience);

        Certificate result = businessService.getById(ID);

        assertEquals(leadershipExperience, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).findLeadershipExperience(ID);
    }

    @DisplayName("Should delete leadership experience")
    @Test
    void shouldDeleteLeadershipExperience() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(leadershipExperience);
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }
}
