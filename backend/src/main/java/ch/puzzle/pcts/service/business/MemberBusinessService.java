package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberBusinessService
        extends
            BusinessBase<Member, MemberValidationService, MemberRepository, MemberPersistenceService> {

    public MemberBusinessService(MemberValidationService validationService,
                                 MemberPersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<Member> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return MEMBER;
    }
}
