package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.member.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberValidationService extends ValidationBase<Member> {

    @Override
    public void validateOnCreate(Member member) {
        throwExceptionWhenModelIsNull(member);
        throwExceptionWhenIdIsNotNull(member.getId());

        validate(member);
    }

    @Override
    public void validateOnUpdate(Long id, Member member) {
        throwExceptionWhenModelIsNull(member);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, member.getId());

        validate(member);
    }
}
