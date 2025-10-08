package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {
    private final CertificateRepository repository;

    public CertificatePersistenceService(CertificateRepository certificateRepository) {
        super(certificateRepository);
        this.repository = certificateRepository;
    }

    public List<Certificate> getAllCertificates() {
        return repository.findByCertificateType(CertificateType.CERTIFICATE);
    }

    public List<Certificate> getAllLeadershipExperiences() {
        return repository.findByCertificateTypeNot(CertificateType.CERTIFICATE);
    }
}
