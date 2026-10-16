package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateCalculationValidationService extends CalculationChildValidationBase<CertificateCalculation> {

    @Override
    public void validateOnCreate(CertificateCalculation model) {
        super.validateOnCreate(model);
        validateMemberForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, CertificateCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
    }

    public void validateDuplicateCertificateId(CertificateCalculation certificateCalculation,
                                               List<CertificateCalculation> certificateCalculationList) {
        validateNoDuplicate(certificateCalculation,
                            certificateCalculationList,
                            CERTIFICATE,
                            calculationChild -> calculationChild.getCertificate().getCertificateType().getName());
    }

    public void validateMemberForCalculation(CertificateCalculation model) {
        validateMemberMatchesCalculation(model,
                                         CERTIFICATE,
                                         calculationChild -> calculationChild.getCertificate().getMember());
    }
}
