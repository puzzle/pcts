package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.repository.MemberOverviewRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewPersistenceService {

    MemberOverviewRepository repository;

    public MemberOverviewPersistenceService(MemberOverviewRepository repository) {
        this.repository = repository;
    }

    public Optional<MemberOverview> getById(Long id) {
        return repository.findOverviewById(id);
    }
}
