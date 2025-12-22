package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {
    private final CertificateRepository repository;

    public CertificatePersistenceService(CertificateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public String entityName() {
        return CERTIFICATE;
    }

    public Certificate findLeadershipExperience(Long id) {
        return repository
                .findByIdAndCertificateType_CertificateKindNot(id, CertificateKind.CERTIFICATE)
                .orElseThrow(() -> {
                    Map<FieldKey, String> attributes = Map
                            .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

                    GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

                    return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
                });
    }
}
