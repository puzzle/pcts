package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.model.member.Member;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class MemberValidationService extends ValidationBase<Member> {

    @Override
    public void validateOnCreate(Member member) {
        super.validateOnCreate(member);
        validateDateOfHireIsBeforeBirthDate(member.getDateOfHire(), member.getBirthDate());
    }

    @Override
    public void validateOnUpdate(Long id, Member member) {
        super.validateOnUpdate(id, member);
        validateDateOfHireIsBeforeBirthDate(member.getDateOfHire(), member.getBirthDate());
    }

    public void validateDateOfHireIsBeforeBirthDate(LocalDate dateOfHire, LocalDate birthDate) {
        validateDateIsBefore(MEMBER, "dateOfBirth", birthDate, "dateOfHire", dateOfHire);
    }
}
