package ch.puzzle.pcts.service.business;

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
    MemberPersistenceService persistenceService;

    public MemberBusinessService(MemberValidationService validationService, MemberPersistenceService persistenceService,
                                 RoleBusinessService roleBusinessService,
                                 CalculationBusinessService calculationBusinessService) {
        super(validationService, persistenceService);
        this.roleBusinessService = roleBusinessService;
        this.calculationBusinessService = calculationBusinessService;
        this.persistenceService = persistenceService;
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

    public List<Calculation> getAllActiveCalculationsByMemberId(Long memberId) {
        Member member = getById(memberId);
        return calculationBusinessService.getAllByMemberAndState(member, CalculationState.ACTIVE);
    }

    @Override
    public Member update(Long id, Member member) {
        Member toBeUpdated = getById(id);

        validationService.validateOnUpdate(id, member);

        member.setId(id);
        member
                .keepSyncData(toBeUpdated.getPtimeId(),
                              toBeUpdated.getLastSuccessfulSync(),
                              toBeUpdated.getSyncErrorCount());

        return persistenceService.save(member);
    }

    public Member findByPtimeId(Long id) {
        return persistenceService.findByPtimeId(id);
    }

    public Member findByAbbreviation(String abbreviation) {
        return persistenceService.findByAbbreviation(abbreviation);
    }
}