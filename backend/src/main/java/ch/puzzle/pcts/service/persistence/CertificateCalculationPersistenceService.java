package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.repository.CertificateCalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateCalculationPersistenceService
        extends
            PersistenceBase<CertificateCalculation, CertificateCalculationRepository> {
    private final CertificateCalculationRepository repository;

    public CertificateCalculationPersistenceService(CertificateCalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<CertificateCalculation> getByCalculationId(Long calculationId) {
        return this.repository.findByCalculationId(calculationId);
    }

    public List<CertificateCalculation> getByCertificateId(Long certificateId) {
        return this.repository.findByCertificateId(certificateId);
    }

}
