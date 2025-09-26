package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificatePersistenceService {

    private EntityManager entityManager;

    private final CertificateRepository repository;

    @Autowired
    public CertificatePersistenceService(CertificateRepository certificateRepository, EntityManager entityManager) {
        this.repository = certificateRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Certificate create(Certificate certificate) {
        repository.saveAndFlush(certificate);
        entityManager.refresh(certificate);
        return certificate;
    }

    public Optional<Certificate> getById(Long id) {
        return repository.findById(id);
    }

    public List<Certificate> getAll() {
        return repository.findAll();
    }

    public Certificate update(Long id, Certificate certificate) {
        certificate.setId(id);
        repository.saveAndFlush(certificate);
        entityManager.refresh(certificate);
        return certificate;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
