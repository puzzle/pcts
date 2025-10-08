package ch.puzzle.pcts.service.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.service.persistence.TagPersistenceService;
import ch.puzzle.pcts.service.validation.TagValidationService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagBusinessServiceTest {

    @Mock
    private TagPersistenceService persistenceService;

    @Mock
    private TagValidationService validationService;

    @InjectMocks
    private TagBusinessService businessService;

    @DisplayName("Should return existing tag if it is already persisted")
    @Test
    void shouldReturnExistingTagIfFound() {
        Tag rawTag = new Tag(null, "Test");
        Tag existingTag = new Tag(1L, "Test");

        when(persistenceService.findWithIgnoreCase("Test")).thenReturn(Optional.of(existingTag));

        Set<Tag> result = businessService.resolveTags(Set.of(rawTag));

        assertThat(result).containsExactly(existingTag);
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should create a new tag if it does not exist yet")
    @Test
    void shouldCreateTagIfNotFound() {
        Tag rawTag = new Tag(null, "NewTag");
        Tag createdTag = new Tag(2L, "NewTag");

        when(persistenceService.findWithIgnoreCase("NewTag")).thenReturn(Optional.empty());
        when(persistenceService.save(any(Tag.class))).thenReturn(createdTag);

        Set<Tag> result = businessService.resolveTags(Set.of(rawTag));

        assertThat(result).containsExactly(createdTag);

        ArgumentCaptor<Tag> captor = ArgumentCaptor.forClass(Tag.class);
        verify(persistenceService).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("NewTag");
    }

    @DisplayName("Should resolve multiple tags by returning existing ones and creating missing ones")
    @Test
    void shouldHandleMultipleTagsWithMixOfExistingAndNew() {
        Tag tag1 = new Tag(null, "Existing");
        Tag tag2 = new Tag(null, "Fresh");

        Tag existingTag = new Tag(1L, "Existing");
        Tag createdTag = new Tag(2L, "Fresh");

        when(persistenceService.findWithIgnoreCase("Existing")).thenReturn(Optional.of(existingTag));
        when(persistenceService.findWithIgnoreCase("Fresh")).thenReturn(Optional.empty());
        when(persistenceService.save(any(Tag.class))).thenReturn(createdTag);

        Set<Tag> result = businessService.resolveTags(Set.of(tag1, tag2));

        assertThat(result).containsExactlyInAnyOrder(existingTag, createdTag);
    }

    @DisplayName("Should delete unused tags")
    @Test
    void shouldDeleteUnusedTags() {
        Tag unusedTag = new Tag(2L, "Unused Tag");

        when(persistenceService.findAllUnusedTags()).thenReturn(Set.of(unusedTag));

        businessService.deleteUnusedTags();

        verify(persistenceService).deleteAll(Set.of(unusedTag));
    }
}
