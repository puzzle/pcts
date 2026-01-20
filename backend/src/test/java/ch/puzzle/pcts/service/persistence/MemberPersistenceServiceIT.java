package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService service) {
        super(service);
    }

    @Override
    Member getModel() {
        return MEMBER_2;
    }

    @Override
    List<Member> getAll() {
        return MEMBERS;
    }
}
