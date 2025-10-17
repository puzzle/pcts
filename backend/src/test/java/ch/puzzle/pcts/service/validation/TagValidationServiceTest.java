package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.certificate.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagValidationServiceTest extends ValidationBaseServiceTest<Tag, TagValidationService> {

    @InjectMocks
    TagValidationService service;

    @Override
    Tag getModel() {
        return new Tag(null, "Tag");
    }

    @Override
    TagValidationService getService() {
        return service;
    }
}
