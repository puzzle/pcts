package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificatePersistenceService {

    private final CertificateRepository repository;

    @Autowired
    public CertificatePersistenceService(CertificateRepository certificateRepository) {
        this.repository = certificateRepository;
    }

    public Certificate create(Certificate certificate) {
        return repository.save(certificate);
    }

    public Optional<Certificate> getById(Long id) {
        return repository.findById(id);
    }

    public List<Certificate> getAll() {
        return repository.findAll();
    }

    public Certificate update(Long id, Certificate certificate) {
        certificate.setId(id);
        return repository.save(certificate);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
