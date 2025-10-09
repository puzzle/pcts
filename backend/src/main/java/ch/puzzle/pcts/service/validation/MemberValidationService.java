package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component

public class MemberValidationService {

    public MemberValidationService() {
    }

    public void validateOnGetById(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnCreate(Member member) {
        // TODO Validation gets added in Story #144 or #111
    }

    public void validateOnDelete(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnUpdate(Member member) {
        throwExceptionWhenIdIsNull(member.getId());
    }

    public void throwExceptionWhenIdIsNull(Long i) {
        if (i == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, ErrorKey.ID_IS_NULL);
        }
    }
}
