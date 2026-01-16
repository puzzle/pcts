package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.util.TestData.TAG_1_ID;
import static ch.puzzle.pcts.util.TestData.TOO_LONG_STRING;
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
                            .of(createTag(TOO_LONG_STRING),
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
                                                    TOO_LONG_STRING))));
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
        Tag tag = getValidModel();

        TagValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Tag>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(TAG_1_ID, tag);

        verify(spyService).validateOnUpdate(TAG_1_ID, tag);
        verifyNoMoreInteractions(persistenceService);
    }
}
