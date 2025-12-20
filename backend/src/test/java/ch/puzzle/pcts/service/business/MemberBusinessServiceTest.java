package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberBusinessServiceTest
        extends
            BaseBusinessTest<Member, MemberPersistenceService, MemberValidationService, MemberBusinessService> {

    @Mock
    private Member member;

    @Mock
    private List<Member> members;

    @Mock
    private MemberPersistenceService persistenceService;

    @Mock
    private MemberValidationService validationService;

    @InjectMocks
    private MemberBusinessService businessService;

    @Override
    Member getModel() {
        return member;
    }

    @Override
    MemberPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    MemberValidationService getValidationService() {
        return validationService;
    }

    @Override
    MemberBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(members);
        when(members.size()).thenReturn(2);

        List<Member> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(members, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Member> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should find member if it exists")
    @Test
    void shouldFindIfExists() {
        when(persistenceService.getById(1L)).thenReturn(Optional.of(member));

        Optional<Member> result = businessService.findIfExists(1L);

        assertTrue(result.isPresent());
        assertEquals(member, result.get());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should return empty optional if member does not exist")
    @Test
    void shouldReturnEmptyIfDoesNotExist() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        Optional<Member> result = businessService.findIfExists(1L);

        assertFalse(result.isPresent());
        verify(persistenceService).getById(1L);
    }
}
