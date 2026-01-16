package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.validation.util.CalculationChildValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateCalculationValidationService extends ValidationBase<CertificateCalculation> {

    @Override
    public void validateOnCreate(CertificateCalculation model) {
        super.validateOnCreate(model);
        this.validateMemberForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, CertificateCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
    }

    public void validateDuplicateCertificateId(CertificateCalculation certificateCalculation,
                                               List<CertificateCalculation> certificateCalculationList) {
        if (CalculationChildValidationUtil
                .validateDuplicateCalculationChildId(certificateCalculation, certificateCalculationList)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "certificate",
                        FieldKey.IS,
                        certificateCalculation.getCertificate().getCertificateType().getName());

            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List.of(new GenericErrorDto(ErrorKey.DUPLICATE_CALCULATION, attributes)));
        }
    }

    public void validateMemberForCalculation(CertificateCalculation model) {
        Member certificateMember = model.getCertificate().getMember();
        Member calculationMember = model.getCalculation().getMember();

        if (!certificateMember.equals(calculationMember)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "certificate",
                        FieldKey.CONDITION_FIELD,
                        "member");

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
