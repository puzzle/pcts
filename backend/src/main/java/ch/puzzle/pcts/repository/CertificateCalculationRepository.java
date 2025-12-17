package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateCalculationRepository extends JpaRepository<CertificateCalculation, Long> {
    List<CertificateCalculation> findByCalculationId(Long calculationId);
    List<CertificateCalculation> findByCertificateId(Long certificateId);
}
