package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberPersistenceService extends PersistenceBase<Member, MemberRepository> {
    private final MemberRepository repository;

    public MemberPersistenceService(MemberRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public String entityName() {
        return MEMBER;
    }

    public Optional<Member> findByPtimeIdAndIdNot(Long ptimeId, Long id) {
        return repository.findByPtimeIdAndIdNotAndPtimeIdNotNull(ptimeId, id);
    }

    public Optional<Member> findByPtimeId(Long ptimeId) {
        return repository.findByPtimeId(ptimeId);
    }

    public Optional<Member> findByAbbreviation(String abbreviation) {
        return repository.findByAbbreviation(abbreviation);
    }
}
