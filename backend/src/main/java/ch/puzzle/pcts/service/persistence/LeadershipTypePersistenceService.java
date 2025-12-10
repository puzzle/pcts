package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipTypePersistenceService extends PersistenceBase<CertificateType, CertificateTypeRepository> {

    private final CertificateTypeRepository repository;

    protected LeadershipTypePersistenceService(CertificateTypeRepository certificateTypeRepository) {
        super(certificateTypeRepository);
        repository = certificateTypeRepository;
    }

    public Optional<CertificateType> getByName(String name) {
        return repository.findByCertificateKindNotAndName(CertificateKind.CERTIFICATE, name);
    }

    @Override
    public List<CertificateType> getAll() {
        return repository.findByCertificateKindNotAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    @Override
    public Optional<CertificateType> getById(Long id) {
        return Optional
                .ofNullable(repository
                        .findByIdAndCertificateKindNot(id, CertificateKind.CERTIFICATE)
                        .orElseThrow(() -> {
                            Map<FieldKey, String> attributes = Map
                                    .of(FieldKey.ENTITY,
                                        LEADERSHIP_EXPERIENCE_TYPE,
                                        FieldKey.FIELD,
                                        "id",
                                        FieldKey.IS,
                                        id.toString());

                            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

                            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
                        }));
    }

    @Override
    public void delete(Long id) {
        repository.deleteCertificateTypeByIdAndCertificateKindNot(id, CertificateKind.CERTIFICATE);
    }
}
