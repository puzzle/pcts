package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidationService extends ValidationBase<Certificate> {
    @Override
    public void validateOnCreate(Certificate certificate) {
        super.validateOnCreate(certificate);
        validateCompletedAtIsBeforeValidUntil(certificate.getCompletedAt(), certificate.getValidUntil());
    }

    @Override
    public void validateOnUpdate(Long id, Certificate certificate) {
        super.validateOnUpdate(id, certificate);
        validateCompletedAtIsBeforeValidUntil(certificate.getCompletedAt(), certificate.getValidUntil());
    }

    public void validateCompletedAtIsBeforeValidUntil(LocalDate completedAt, LocalDate validUntil) {
        if (validUntil != null && completedAt != null && completedAt.isAfter(validUntil)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CERTIFICATE,
                        FieldKey.FIELD,
                        "completedAt",
                        FieldKey.IS,
                        completedAt.toString(),
                        FieldKey.CONDITION_FIELD,
                        "validUntil",
                        FieldKey.MAX,
                        validUntil.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_BEFORE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
