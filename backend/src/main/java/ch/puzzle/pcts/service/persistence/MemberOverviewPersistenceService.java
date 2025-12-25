package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.repository.MemberOverviewRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewPersistenceService {

    MemberOverviewRepository repository;

    public MemberOverviewPersistenceService(MemberOverviewRepository repository) {
        this.repository = repository;
    }

    public List<MemberOverview> getById(Long id) {
        return repository.findAllByMemberId(id);
    }
}
