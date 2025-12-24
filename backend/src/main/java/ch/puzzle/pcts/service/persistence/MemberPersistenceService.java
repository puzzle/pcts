package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberPersistenceService extends PersistenceBase<Member, MemberRepository> {
    public MemberPersistenceService(MemberRepository repository) {
        super(repository);
    }

    @Override
    public String entityName() {
        return MEMBER;
    }
}
