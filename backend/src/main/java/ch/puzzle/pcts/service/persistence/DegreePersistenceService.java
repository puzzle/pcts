package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.DEGREE;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.DegreeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreePersistenceService extends PersistenceBase<Degree, DegreeRepository> {
    public DegreePersistenceService(DegreeRepository repository) {
        super(repository);
    }

    @Override
    public String entityName() {
        return DEGREE;
    }

    public List<Degree> findAllByMember(Member member) {
        return repository.findAllByMember(member);
    }

}
