package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeTypeBusinessServiceTest
        extends
            BaseBusinessTest<DegreeType, DegreeTypePersistenceService, DegreeTypeValidationService, DegreeTypeBusinessService> {

    @Mock
    DegreeType degreeType;

    @Mock
    List<DegreeType> degreeTypes;

    @Mock
    DegreeTypePersistenceService persistenceService;

    @Mock
    DegreeTypeValidationService validationService;

    @InjectMocks
    DegreeTypeBusinessService businessService;

    @Override
    DegreeType getModel() {
        return degreeType;
    }

    @Override
    DegreeTypePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    DegreeTypeValidationService getValidationService() {
        return validationService;
    }

    @Override
    DegreeTypeBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(degreeTypes);
        when(degreeTypes.size()).thenReturn(2);

        List<DegreeType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(degreeTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<DegreeType> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
