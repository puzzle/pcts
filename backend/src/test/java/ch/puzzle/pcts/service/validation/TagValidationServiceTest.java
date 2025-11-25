package ch.puzzle.pcts.service.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.service.persistence.TagPersistenceService;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagValidationServiceTest extends ValidationBaseServiceTest<Tag, TagValidationService> {

    @Mock
    TagPersistenceService persistenceService;

    @InjectMocks
    TagValidationService validationService;

    @Override
    Tag getValidModel() {
        return createTag("Tag");
    }

    @Override
    TagValidationService getService() {
        return validationService;
    }

    private static Tag createTag(String name) {
        Tag t = new Tag();
        t.setName(name);
        return t;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments.of(createTag(null), List.of(Map.of(FieldKey.CLASS, "Tag", FieldKey.FIELD, "name"))),
                    Arguments.of(createTag(""), List.of(Map.of(FieldKey.CLASS, "Tag", FieldKey.FIELD, "name"))),
                    Arguments.of(createTag("  "), List.of(Map.of(FieldKey.CLASS, "Tag", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createTag("S"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Tag",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createTag("  S "),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Tag",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createTag(tooLongName),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Tag",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    tooLongName))));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Tag tag = getValidModel();

        TagValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Tag>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(tag);

        verify(spyService).validateOnCreate(tag);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Tag tag = getValidModel();

        TagValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Tag>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, tag);

        verify(spyService).validateOnUpdate(id, tag);
        verifyNoMoreInteractions(persistenceService);
    }
}
