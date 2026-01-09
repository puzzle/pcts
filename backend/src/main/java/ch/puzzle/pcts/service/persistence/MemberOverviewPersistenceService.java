package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.MEMBER_OVERVIEW;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.repository.MemberOverviewRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewPersistenceService {

    private final MemberOverviewRepository repository;

    public MemberOverviewPersistenceService(MemberOverviewRepository repository) {
        this.repository = repository;
    }

    public List<MemberOverview> getById(Long id) {
        List<MemberOverview> result = repository.findAllByMemberId(id);

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
