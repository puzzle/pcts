package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component

public class MemberValidationService {

    private final MemberPersistenceService persistenceService;

    public MemberValidationService(MemberPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnCreate(Member member) {
    }

    public void validateOnDelete(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnUpdate(Long id, Member member) {
        throwExceptionWhenIdIsNull(member.getId());
    }

    public void throwExceptionWhenIdIsNull(Long i) {
        if (i == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, ErrorKey.ID_IS_NULL);
        }
    }
}
