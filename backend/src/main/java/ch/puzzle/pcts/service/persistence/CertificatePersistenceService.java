package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateRepository;
import org.springframework.stereotype.Service;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {
    public CertificatePersistenceService(CertificateRepository repository) {
        super(repository);
    }
}
