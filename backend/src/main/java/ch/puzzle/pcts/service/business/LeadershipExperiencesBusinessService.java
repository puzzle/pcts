package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperiencesBusinessService extends BusinessBase<Certificate> {
    private final CertificatePersistenceService persistenceService;

    LeadershipExperiencesBusinessService(CertificatePersistenceService persistenceService,
                                         LeadershipExperienceValidationService validationService) {
        super(validationService, persistenceService);
        this.persistenceService = persistenceService;
    }
    @Override
    public Certificate getById(Long id) {
        return persistenceService.findLeadershipExperience(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE;
    }
}
