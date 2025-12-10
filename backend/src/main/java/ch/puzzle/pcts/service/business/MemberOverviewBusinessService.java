package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER_OVERVIEW;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.persistence.MemberOverviewPersistenceService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewBusinessService {

    private final MemberOverviewPersistenceService persistenceService;

    public MemberOverviewBusinessService(MemberOverviewPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public List<MemberOverview> getById(Long id) {
        return persistenceService.getById(id);
    }

    protected String entityName() {
        return MEMBER_OVERVIEW;
    }
}
