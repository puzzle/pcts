package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberBusinessServiceTest {

    @Mock
    private MemberValidationService validationService;

    @Mock
    private MemberPersistenceService persistenceService;

    @Mock
    private Member member;

    @Mock
    private List<Member> members;

    @InjectMocks
    private MemberBusinessService businessService;

    @DisplayName("Should get member by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(1L)).thenReturn(Optional.of(member));

        Member result = businessService.getById(1L);

        assertEquals(member, result);
        verify(persistenceService).getById(1L);
        verify(validationService).validateOnGetById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertIterableEquals(List.of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, MEMBER)),
                             exception.getErrorAttributes());
        verify(persistenceService).getById(1L);
        verify(validationService).validateOnGetById(1L);
    }

    @DisplayName("Should get all members")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(members);
        when(members.size()).thenReturn(2);

        List<Member> result = businessService.getAll();

        assertEquals(members, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<Member> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create member")
    @Test
    void shouldCreate() {
        when(persistenceService.save(member)).thenReturn(member);

        Member result = businessService.create(member);

        assertEquals(member, result);
        verify(validationService).validateOnCreate(member);
        verify(persistenceService).save(member);
    }

    @DisplayName("Should update member")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(member)).thenReturn(member);
        when(persistenceService.getById(id)).thenReturn(Optional.of(member));

        Member result = businessService.update(id, member);

        assertEquals(member, result);
        verify(validationService).validateOnUpdate(id, member);
        verify(persistenceService).save(member);
    }

    @DisplayName("Should throw exception when updating non-existing member")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, member));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete member")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(member));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing member")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}
