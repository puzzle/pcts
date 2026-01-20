package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.persistence.CertificateCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CertificateCalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateCalculationBusinessService extends BusinessBase<CertificateCalculation> {
    private final CertificateCalculationPersistenceService certificateCalculationPersistenceService;
    private final CertificateCalculationValidationService certificateCalculationValidationService;

    protected CertificateCalculationBusinessService(CertificateCalculationValidationService validationService,
                                                    CertificateCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.certificateCalculationPersistenceService = persistenceService;
        this.certificateCalculationValidationService = validationService;
    }

    public List<CertificateCalculation> getByCalculationId(Long calculationId) {
        return certificateCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<CertificateCalculation> getByCertificateId(Long certificateId) {
        return certificateCalculationPersistenceService.getByCertificateId(certificateId);
    }

    public BigDecimal getCertificatePoints(Long id) {
        List<CertificateCalculation> certificateCalculations = getByCalculationId(id);
        return certificateCalculations
                .stream()
                .filter(this::isEligibleForPoints)
                .map(this::extractPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isEligibleForPoints(CertificateCalculation certificateCalculation) {
        boolean hasManagementRole = certificateCalculation.getCalculation().getRole().getIsManagement();

        boolean isLeadership = certificateCalculation
                .getCertificate()
                .getCertificateType()
                .getCertificateKind()
                .isLeadershipExperienceType();

        return hasManagementRole || !isLeadership;
    }

    private BigDecimal extractPoints(CertificateCalculation certificateCalculation) {
        return certificateCalculation.getCertificate().getCertificateType().getPoints();
    }

    @Override
    public CertificateCalculation update(Long id, CertificateCalculation certificateCalculation) {
        certificateCalculationValidationService.validateOnUpdate(id, certificateCalculation);
        List<CertificateCalculation> existing = getByCertificateId(certificateCalculation.getCertificate().getId());
        certificateCalculationValidationService.validateDuplicateCertificateId(certificateCalculation, existing);
        return certificateCalculationPersistenceService.save(certificateCalculation);
    }

    @Override
    public CertificateCalculation create(CertificateCalculation certificateCalculation) {
        certificateCalculationValidationService.validateOnCreate(certificateCalculation);
        List<CertificateCalculation> existing = getByCertificateId(certificateCalculation.getCertificate().getId());
        certificateCalculationValidationService.validateDuplicateCertificateId(certificateCalculation, existing);
        return certificateCalculationPersistenceService.save(certificateCalculation);
    }

    public List<CertificateCalculation> createCertificateCalculations(Calculation calculation) {
        return calculation.getCertificateCalculations().stream().map(certCalc -> {
            certCalc.setCalculation(calculation);
            return create(certCalc);
        }).toList();
    }

    public List<CertificateCalculation> updateCertificateCalculations(Calculation calculation) {
        List<CertificateCalculation> existing = getByCalculationId(calculation.getId());

        List<CertificateCalculation> certificateCalculations = calculation
                .getCertificateCalculations()
                .stream()
                .map(certificateCalculation -> {
                    certificateCalculation.setCalculation(calculation);
                    return certificateCalculation.getId() == null ? create(certificateCalculation)
                            : update(certificateCalculation.getId(), certificateCalculation);
                })
                .toList();
        deleteUnusedCertificateCalculations(existing, certificateCalculations);

        return certificateCalculations;
    }

    private void deleteUnusedCertificateCalculations(List<CertificateCalculation> existing,
                                                     List<CertificateCalculation> updated) {
        // remove all created/updated certificate calculations from the list
        existing.removeAll(updated);

        // delete the remaining, which are unused
        certificateCalculationPersistenceService
                .deleteAllByIdInBatch(existing.stream().map(CertificateCalculation::getId).toList());
    }
}
