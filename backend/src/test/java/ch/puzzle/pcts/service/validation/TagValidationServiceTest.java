package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.certificate.Tag;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagValidationServiceTest extends ValidationBaseServiceTest<Tag, TagValidationService> {

    @InjectMocks
    TagValidationService validationService;

    @Override
    Tag getModel() {
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
                .of(Arguments.of(createTag(null), "Tag.name must not be null."),
                    Arguments.of(createTag(""), "Tag.name must not be blank."),
                    Arguments.of(createTag("  "), "Tag.name must not be blank."),
                    Arguments.of(createTag("S"), "Tag.name size must be between 2 and 250, given S."),
                    Arguments.of(createTag("  S "), "Tag.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createTag(tooLongName),
                                String.format("Tag.name size must be between 2 and 250, given %s.", tooLongName)));
    }
}
