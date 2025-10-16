package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberBusinessService {

    private final MemberValidationService validationService;
    private final MemberPersistenceService persistenceService;

    public MemberBusinessService(MemberValidationService validationService,
                                 MemberPersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<Member> getAll() {
        return persistenceService.getAll();
    }

    public Member getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Member with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public Member create(Member member) {
        validationService.validateOnCreate(member);
        return persistenceService.create(member);
    }

    public Member update(Long id, Member member) {
        validationService.validateOnUpdate(id);
        return persistenceService.update(id, member);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
