package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateCalculationRepository extends CalculationChildRepository<CertificateCalculation, Long> {
    List<CertificateCalculation> findByCertificateId(Long certificateId);
}
