package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberPersistenceService extends PersistenceBase<Member, MemberRepository> {
    private final MemberRepository repository;

    public MemberPersistenceService(MemberRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Member create(Member member) {
        return repository.save(member);
    }

    public Optional<Member> getById(Long id) {
        return repository.findById(id);
    }

    public List<Member> getAll() {
        return repository.findAll();
    }

    public Member update(Long id, Member member) {
        member.setId(id);
        return repository.save(member);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
