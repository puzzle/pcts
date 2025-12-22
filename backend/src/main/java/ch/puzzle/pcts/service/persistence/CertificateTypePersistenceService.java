package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypePersistenceService extends PersistenceBase<CertificateType, CertificateTypeRepository> {
    private final CertificateTypeRepository repository;

    public CertificateTypePersistenceService(CertificateTypeRepository certificateTypeRepository) {
        super(certificateTypeRepository);
        this.repository = certificateTypeRepository;
    }

    public Optional<CertificateType> getByName(String name) {
        return repository.findByNameOfCertificateType(name);
    }

    @Override
    public List<CertificateType> getAll() {
        return repository.findAllOfCertificateType();
    }

    @Override
    public String entityName() {
        return CERTIFICATE_TYPE;
    }

    @Override
    public CertificateType getById(Long id) {
        return repository.findByIdOfCertificateType(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CERTIFICATE_TYPE, FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }
}
