package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
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
        validateBirthDateIsBeforeDateOfHire(member.getBirthDate(), member.getDateOfHire());
    }

    @Override
    public void validateOnUpdate(Long id, Member member) {
        super.validateOnUpdate(id, member);
        validateBirthDateIsBeforeDateOfHire(member.getBirthDate(), member.getDateOfHire());
        validatePtimeIdIsUnique(member.getPtimeId(), id);
    }

    private void validateBirthDateIsBeforeDateOfHire(LocalDate birthDate, LocalDate dateOfHire) {
        validateDateIsBefore(MEMBER, "dateOfBirth", birthDate, "dateOfHire", dateOfHire);
    }

    private void validatePtimeIdIsUnique(Long ptimeId, Long id) {
        Optional<Member> result = persistenceService.findByPtimeIdAndIdNot(ptimeId, id);

        if (result.isPresent()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE,
                                                                    Map
                                                                            .of(FieldKey.CLASS,
                                                                                "Member",
                                                                                FieldKey.FIELD,
                                                                                "ptimeId"))));
        }
    }
}
