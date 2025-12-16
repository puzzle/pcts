package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

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

    @Override
    public String entityName() {
        return CERTIFICATE;
    }

    public Optional<Certificate> findLeadershipExperience(Long id) {
        return repository.findByIdAndCertificateType_CertificateKindNot(id, CertificateKind.CERTIFICATE);
    }
}
