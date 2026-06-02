package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE_CALCULATION;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.repository.CertificateCalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateCalculationPersistenceService
        extends
            PersistenceBase<CertificateCalculation, CertificateCalculationRepository> {
    private final CertificateCalculationRepository certificateCalculationRepository;

    public CertificateCalculationPersistenceService(CertificateCalculationRepository certificateCalculationRepository) {
        super(certificateCalculationRepository);
        this.certificateCalculationRepository = certificateCalculationRepository;
    }

    public List<CertificateCalculation> getByCalculationId(Long calculationId) {
        return certificateCalculationRepository.findByCalculationId(calculationId);
    }

    public List<CertificateCalculation> getByCertificateId(Long certificateId) {
        return certificateCalculationRepository.findByCertificateId(certificateId);
    }

    public void deleteAllById(List<Long> ids) {
        certificateCalculationRepository.deleteAllById(ids);
    }

    @Override
    public String entityName() {
        return CERTIFICATE_CALCULATION;
    }
}
