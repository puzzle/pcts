package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.JwtService;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberBusinessService extends BusinessBase<Member> {
    private final JwtService jwtService;
    private final MemberPersistenceService memberPersistenceService;
    RoleBusinessService roleBusinessService;
    CalculationBusinessService calculationBusinessService;

    public MemberBusinessService(MemberValidationService validationService, MemberPersistenceService persistenceService,
                                 RoleBusinessService roleBusinessService,
                                 CalculationBusinessService calculationBusinessService, JwtService jwtService) {
        super(validationService, persistenceService);
        this.jwtService = jwtService;
        this.memberPersistenceService = persistenceService;
        this.roleBusinessService = roleBusinessService;
        this.calculationBusinessService = calculationBusinessService;
    }

    public Optional<Member> findIfExists(Long id) {
        return memberPersistenceService.findById(id);
    }

    public List<Member> getAll() {
        return persistenceService.getAll();
    }

    public Member getLoggedInMember() {
        Optional<String> email = jwtService.getEmail();
        if (email.isEmpty()) {
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of());
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }

        return memberPersistenceService.getByEmail(email.get());
    }

    public List<Calculation> getAllCalculationsByMemberIdAndRoleId(Long memberId, Long roleId) {
        Member member = this.getById(memberId);
        if (roleId != null) {
            Role role = roleBusinessService.getById(roleId);
            return calculationBusinessService.getAllByMemberAndRole(member, role);
        } else {
            return calculationBusinessService.getAllByMember(member);
        }
    }

    public List<Calculation> getAllActiveCalculationsByMemberId(Long memberId) {
        Member member = getById(memberId);
        return calculationBusinessService.getAllByMemberAndState(member, CalculationState.ACTIVE);
    }

}