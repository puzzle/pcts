package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.calculation.RolePointDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberBusinessService extends BusinessBase<Member> {
    RoleBusinessService roleBusinessService;
    CalculationBusinessService calculationBusinessService;

    public MemberBusinessService(MemberValidationService validationService, MemberPersistenceService persistenceService,
                                 RoleBusinessService roleBusinessService,
                                 CalculationBusinessService calculationBusinessService) {
        super(validationService, persistenceService);
        this.roleBusinessService = roleBusinessService;
        this.calculationBusinessService = calculationBusinessService;
    }

    public List<Member> getAll() {
        return persistenceService.getAll();
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

    public List<RolePointDto> getAllPointsByMemberIdAndRoleId(Long memberId) {
        Member member = this.getById(memberId);
        List<Calculation> calculations = calculationBusinessService
                .getAllByMemberAndState(member, CalculationState.ACTIVE);

        return calculations.stream().map(c -> new RolePointDto(c.getRole(), c.getPoints())).toList();
    }

}