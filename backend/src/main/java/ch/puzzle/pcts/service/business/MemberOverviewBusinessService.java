package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.MEMBER_OVERVIEW;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.persistence.MemberOverviewPersistenceService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewBusinessService {

    private final MemberOverviewPersistenceService persistenceService;

    public MemberOverviewBusinessService(MemberOverviewPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public MemberOverview getById(Long id) {
        return persistenceService.getById(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }

    protected String entityName() {
        return MEMBER_OVERVIEW;
    }
}
