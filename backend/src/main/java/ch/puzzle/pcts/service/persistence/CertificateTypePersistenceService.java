package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import java.util.List;
import java.util.Optional;
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

    public List<CertificateType> findAllWhereLinkIsNotNull() {
        return repository.findAllByLinkNotNullAndDeletedAtIsNull();
    }

    public boolean nameAndPublisherExcludingIdAlreadyUsed(String name, String publisher, Long id) {
        return repository.existsByNameAndPublisherAndIdNot(name, publisher, id);
    }

    @Override
    public String entityName() {
        return CERTIFICATE_TYPE;
    }
}
