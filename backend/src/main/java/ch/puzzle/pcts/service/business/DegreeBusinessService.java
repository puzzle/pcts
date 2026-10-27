package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeBusinessService extends BusinessBase<Degree> {
    private final DegreePersistenceService degreePersistenceService;

    public DegreeBusinessService(DegreeValidationService validationService,
                                 DegreePersistenceService persistenceService) {
        super(validationService, persistenceService);
        degreePersistenceService = persistenceService;
    }
    public List<Degree> getAll() {
        return degreePersistenceService.getAll();
    }

    public List<Degree> findAllByMember(Member member) {
        return degreePersistenceService.findAllByMember(member);
    }
}
