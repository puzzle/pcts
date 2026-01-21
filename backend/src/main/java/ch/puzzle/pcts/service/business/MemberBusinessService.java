package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.calculation.RolePointDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
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
        List<Calculation> calculations = calculationBusinessService.getAllByMember(member);

        return calculations
                .stream()
                .collect(Collectors
                        .groupingBy(Calculation::getRole,
                                    Collectors.reducing(BigDecimal.ZERO, Calculation::getPoints, BigDecimal::add)))
                .entrySet()
                .stream()
                .map(e -> new RolePointDto(e.getKey(), e.getValue()))
                .toList();
    }

}