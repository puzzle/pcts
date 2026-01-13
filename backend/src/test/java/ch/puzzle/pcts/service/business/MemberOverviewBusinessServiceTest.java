package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.persistence.MemberOverviewPersistenceService;
import ch.puzzle.pcts.service.validation.MemberOverviewValidationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberOverviewBusinessServiceTest {

    @Mock
    private MemberOverviewValidationService validationService;

    @Mock
    private MemberOverviewPersistenceService persistenceService;

    @Mock
    private List<MemberOverview> memberOverviews;

    @InjectMocks
    private MemberOverviewBusinessService businessService;

    @DisplayName("Should get organisationUnit by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(memberOverviews);

        List<MemberOverview> result = businessService.getById(id);

        assertEquals(memberOverviews, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }
}
