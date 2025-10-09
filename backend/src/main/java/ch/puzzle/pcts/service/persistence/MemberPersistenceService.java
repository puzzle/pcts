package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceService {
    private final MemberRepository repository;

    @Autowired
    public MemberPersistenceService(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Member create(Member member) {
        System.out.println(member.getOrganisationUnit());
        return repository.save(member);
    }

    public Optional<Member> getById(long id) {
        return repository.findById(id);
    }

    public List<Member> getAll() {
        return repository.findAll();
    }

    @Transactional
    public Member update(Long id, Member member) {
        member.setId(id);
        return repository.save(member);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
