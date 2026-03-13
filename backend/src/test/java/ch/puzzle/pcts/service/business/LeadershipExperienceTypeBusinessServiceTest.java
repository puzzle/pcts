package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceTypePersistenceService;
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
            BaseBusinessTest<LeadershipExperienceType, LeadershipExperienceTypePersistenceService, LeadershipExperienceTypeValidationService, LeadershipExperienceTypeBusinessService> {

    @Mock
    private LeadershipExperienceType leadershipExperienceType;

    @Mock
    private List<LeadershipExperienceType> leadershipExperiencesTypes;

    @Mock
    private LeadershipExperienceTypePersistenceService persistenceService;

    @Mock
    private LeadershipExperienceTypeValidationService validationService;

    @InjectMocks
    private LeadershipExperienceTypeBusinessService businessService;

    @Override
    LeadershipExperienceType getModel() {
        return leadershipExperienceType;
    }

    @Override
    LeadershipExperienceTypePersistenceService getPersistenceService() {
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

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(leadershipExperiencesTypes);
        when(leadershipExperiencesTypes.size()).thenReturn(2);

        List<LeadershipExperienceType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(leadershipExperiencesTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<LeadershipExperienceType> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
