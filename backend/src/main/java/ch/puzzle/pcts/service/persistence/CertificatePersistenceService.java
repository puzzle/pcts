package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {
    private final CertificateRepository repository;

    public CertificatePersistenceService(CertificateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<Certificate> findLeadershipExperience(Long id) {
        return repository.findByIdAndCertificateType_CertificateKindIsNot(id, CertificateKind.CERTIFICATE);
    }
}
