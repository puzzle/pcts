package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class MemberValidationService extends ValidationBase<Member> {
    private final MemberPersistenceService persistenceService;

    public MemberValidationService(MemberPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(Member member) {
        super.validateOnCreate(member);
        if (UniqueNameValidationUtil.nameAlreadyUsed(member.getEmail(), persistenceService::findByEmail)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.CLASS, MEMBER, FieldKey.FIELD, "email", FieldKey.IS, member.getEmail());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    @Override
    public void validateOnCreate(Member member) {
        super.validateOnCreate(member);
        validateBirthDateIsBeforeDateOfHire(member.getBirthDate(), member.getDateOfHire());
    }

    @Override
    public void validateOnUpdate(Long id, Member member) {
        super.validateOnUpdate(id, member);
        validateBirthDateIsBeforeDateOfHire(member.getBirthDate(), member.getDateOfHire());

        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, member.getEmail(), persistenceService::findByEmail)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.CLASS, MEMBER, FieldKey.FIELD, "email", FieldKey.IS, member.getEmail());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    private void validateBirthDateIsBeforeDateOfHire(LocalDate birthDate, LocalDate dateOfHire) {
        validateDateIsBefore(MEMBER, "dateOfBirth", birthDate, "dateOfHire", dateOfHire);
    }
}
