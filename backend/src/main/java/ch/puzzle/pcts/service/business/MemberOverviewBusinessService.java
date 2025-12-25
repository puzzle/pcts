package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER_OVERVIEW;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.persistence.MemberOverviewPersistenceService;
import ch.puzzle.pcts.service.validation.MemberOverviewValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewBusinessService {

    private final MemberOverviewPersistenceService persistenceService;

    private final MemberOverviewValidationService validationService;

    public MemberOverviewBusinessService(MemberOverviewPersistenceService persistenceService,
                                         MemberOverviewValidationService validationService) {
        this.persistenceService = persistenceService;
        this.validationService = validationService;
    }

    public List<MemberOverview> getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService.getById(id);
    }

    protected String entityName() {
        return MEMBER_OVERVIEW;
    }
}
