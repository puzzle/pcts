package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceTypeValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceTypeBusinessServiceTest
        extends
            BaseBusinessTest<ExperienceType, ExperienceTypePersistenceService, ExperienceTypeValidationService, ExperienceTypeBusinessService> {

    @Mock
    ExperienceType experienceType;

    @Mock
    List<ExperienceType> experienceTypes;

    @Mock
    ExperienceTypePersistenceService persistenceService;

    @Mock
    ExperienceTypeValidationService validationService;

    @InjectMocks
    ExperienceTypeBusinessService businessService;

    @Override
    ExperienceType getModel() {
        return experienceType;
    }

    @Override
    ExperienceTypePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    ExperienceTypeValidationService getValidationService() {
        return validationService;
    }

    @Override
    ExperienceTypeBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(experienceTypes);
        when(experienceTypes.size()).thenReturn(2);

        List<ExperienceType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(experienceTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<ExperienceType> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
