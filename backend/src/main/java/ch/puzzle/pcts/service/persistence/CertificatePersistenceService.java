package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {
    private final CertificateRepository repository;

    public CertificatePersistenceService(CertificateRepository certificateRepository) {
        super(certificateRepository);
        this.repository = certificateRepository;
    }

    public Optional<Certificate> getByName(String name) {
        return repository.findByName(name);
    }

    public List<Certificate> getAllCertificates() {
        return repository.findByCertificateTypeAndDeletedAtIsNull(CertificateType.CERTIFICATE);
    }

    public List<Certificate> getAllLeadershipExperiences() {
        return repository.findByCertificateTypeNotAndDeletedAtIsNull(CertificateType.CERTIFICATE);
    }
}
