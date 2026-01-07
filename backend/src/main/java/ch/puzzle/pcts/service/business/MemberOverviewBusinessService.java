package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER_OVERVIEW;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.persistence.MemberOverviewPersistenceService;
import ch.puzzle.pcts.service.validation.MemberOverviewValidationService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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

        List<MemberOverview> result = persistenceService.getById(id);

        if (result.isEmpty()) {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.NOT_FOUND,
                                                                    Map
                                                                            .of(FieldKey.ENTITY,
                                                                                entityName(),
                                                                                FieldKey.FIELD,
                                                                                "id",
                                                                                FieldKey.IS,
                                                                                id.toString()))));
        }

        return result;
    }

    protected String entityName() {
        return MEMBER_OVERVIEW;
    }
}
