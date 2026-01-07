package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE_CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.persistence.CertificateCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CertificateCalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
        return this.certificateCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<CertificateCalculation> getByCertificateId(Long certificateId) {
        return this.certificateCalculationPersistenceService.getByCertificateId(certificateId);
    }

    public BigDecimal getCertificatePoints(Long id) {
        List<CertificateCalculation> certificateCalculations = this.getByCalculationId(id);
        return certificateCalculations
                .stream()
                .filter(this::isEligibleForPoints)
                .map(this::extractPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isEligibleForPoints(CertificateCalculation certificateCalculation) {
        boolean isManagement = certificateCalculation.getCalculation().getRole().getIsManagement();

        boolean isLeadership = certificateCalculation
                .getCertificate()
                .getCertificateType()
                .getCertificateKind()
                .isLeadershipExperienceType();

        return isManagement || !isLeadership;
    }

    private BigDecimal extractPoints(CertificateCalculation certificateCalculation) {
        return certificateCalculation.getCertificate().getCertificateType().getPoints();
    }

    @Override
    public CertificateCalculation create(CertificateCalculation certificateCalculation) {
        List<CertificateCalculation> existing = this
                .getByCertificateId(certificateCalculation.getCertificate().getId());
        certificateCalculationValidationService.validateOnCreate(certificateCalculation);
        certificateCalculationValidationService.validateDuplicateCertificateId(certificateCalculation, existing);
        return certificateCalculationPersistenceService.save(certificateCalculation);
    }

    @Override
    public CertificateCalculation update(Long id, CertificateCalculation certificateCalculation) {
        if (persistenceService.getById(id).isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }
        certificateCalculationValidationService.validateOnUpdate(id, certificateCalculation);
        certificateCalculationValidationService.validateMemberForCalculation(certificateCalculation);
        certificateCalculation.setId(id);
        return certificateCalculationPersistenceService.save(certificateCalculation);
    }

    public List<CertificateCalculation> createCertificateCalculations(Calculation calculation) {
        return calculation.getCertificates().stream().map(exp -> {
            exp.setCalculation(calculation);
            return this.create(exp);
        }).toList();
    }

    public List<CertificateCalculation> updateCertificateCalculations(Calculation calculation) {
        List<CertificateCalculation> existing = this.getByCalculationId(calculation.getId());

        List<CertificateCalculation> certificateCalculations = calculation
                .getCertificates()
                .stream()
                .map(certificateCalculation -> {
                    certificateCalculation.setCalculation(calculation);
                    return certificateCalculation.getId() == null ? this.create(certificateCalculation)
                            : this.update(certificateCalculation.getId(), certificateCalculation);
                })
                .toList();

        /*
         * Removing all created or updated certificate calculations to later delete the
         * unused certificate calculations
         */
        existing.removeAll(certificateCalculations);
        existing.stream().map(CertificateCalculation::getId).forEach(this::delete);

        return certificateCalculations;
    }

    // private Long findIdByCalculationAndCertificate(CertificateCalculation
    // certificateCalculation,
    // List<CertificateCalculation> certificateCalculationList) {
    // return certificateCalculationList
    // .stream()
    // .filter(ec ->
    // ec.getCalculation().getId().equals(certificateCalculation.getCalculation().getId())
    // &&
    // ec.getCertificate().getId().equals(certificateCalculation.getCertificate().getId()))
    // .map(CertificateCalculation::getId)
    // .findFirst()
    // .orElse(null);
    // }

    @Override
    protected String entityName() {
        return CERTIFICATE_CALCULATION;
    }
}
