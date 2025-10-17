package ch.puzzle.pcts.service.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.service.persistence.TagPersistenceService;
import ch.puzzle.pcts.service.validation.TagValidationService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagBusinessServiceTest {

    @Mock
    private TagPersistenceService persistenceService;

    @Mock
    private TagValidationService validationService;

    @Mock
    private Tag tag;

    @Mock
    private Tag existingTag;

    @InjectMocks
    private TagBusinessService businessService;

    @DisplayName("Should return existing tag if it is already persisted")
    @Test
    void shouldReturnExistingTagIfFound() {
        when(tag.getName()).thenReturn("Test");
        when(persistenceService.findWithIgnoreCase("Test")).thenReturn(Optional.of(existingTag));

        Set<Tag> result = businessService.resolveTags(Set.of(tag));

        assertThat(result).containsExactly(existingTag);
        verify(persistenceService, never()).save(any());
        verify(validationService).validateName(tag);
    }

    @DisplayName("Should create a new tag if it does not exist yet")
    @Test
    void shouldCreateTagIfNotFound() {
        String notPersistedName = "NotYetPersisted";

        when(tag.getName()).thenReturn(notPersistedName);

        when(persistenceService.findWithIgnoreCase(notPersistedName)).thenReturn(Optional.empty());
        // only mock the exact correct case and not any tag, we want to ensure that only
        // expected stubings happen
        when(persistenceService.create(argThat(t -> t.getName().equals(notPersistedName)))).thenReturn(existingTag);

        Set<Tag> result = businessService.resolveTags(Set.of(tag));

        assertThat(result).containsExactly(existingTag);

        // verify that the exact correct tag is created and not any tag here
        verify(persistenceService).save(argThat(t -> t.getName().equals(notPersistedName)));
        // verify that no other tag is created than the expected
        verify(persistenceService, never()).create(argThat(t -> !t.getName().equals(notPersistedName)));
        assertTrue(result.contains(existingTag));
        verify(validationService).validateName(tag);
    }

    @DisplayName("Should resolve multiple tags by returning existing ones and creating missing ones")
    @Test
    void shouldHandleMultipleTagsWithMixOfExistingAndNew() {
        // arrange
        String existingName = "Existing";
        String newName = "New";
        when(tag.getName()).thenReturn(newName);
        when(existingTag.getName()).thenReturn(existingName);
        when(persistenceService.findWithIgnoreCase(existingName)).thenReturn(Optional.of(existingTag));
        when(persistenceService.findWithIgnoreCase(newName)).thenReturn(Optional.empty());
        when(persistenceService.create(argThat(t -> t.getName().equals(newName)))).thenReturn(tag);

        // act
        Set<Tag> result = businessService.resolveTags(Set.of(tag, existingTag));

        // assert
        assertThat(result).containsExactlyInAnyOrder(existingTag, tag);
        verify(persistenceService).create(argThat(t -> t.getName().equals(newName)));
        verify(persistenceService).findWithIgnoreCase(newName);
        verify(persistenceService, never()).create(argThat(t -> t.getName().equals(existingName)));
        verify(persistenceService).findWithIgnoreCase(existingName);
        verify(validationService).validateName(tag);
        verify(validationService).validateName(existingTag);
    }

    @DisplayName("Should delete unused tags")
    @Test
    void shouldDeleteUnusedTags() {
        when(persistenceService.findAllUnusedTags()).thenReturn(Set.of(tag));

        businessService.deleteUnusedTags();

        verify(persistenceService).deleteAll(Set.of(tag));
        verifyNoInteractions(validationService);
        verify(persistenceService).findAllUnusedTags();
    }
}
