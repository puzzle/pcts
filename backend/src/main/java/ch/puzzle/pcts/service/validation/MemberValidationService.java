package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberValidationService {

    public void validateOnGetById(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnCreate(Member member) {
        // Will get content in the future.
    }

    public void validateOnDelete(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnUpdate(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void throwExceptionWhenIdIsNull(Long id) {
        if (id == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, ErrorKey.ID_IS_NULL);
        }
    }
}
