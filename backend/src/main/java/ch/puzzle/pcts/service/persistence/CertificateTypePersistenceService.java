package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
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
        return repository.findByName(name);
    }

    public List<CertificateType> getAllCertificateTypes() {
        return repository.findByCertificateKindAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    public List<CertificateType> getAllLeadershipExperienceTypes() {
        return repository.findByCertificateKindNotAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }
}
