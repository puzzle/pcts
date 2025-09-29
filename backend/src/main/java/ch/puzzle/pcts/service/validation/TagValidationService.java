package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TagValidationService {

    public void validateName(Tag tag) {
        if (tag.getName() == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.TAG_NAME_IS_NULL);
        }
    }
}
