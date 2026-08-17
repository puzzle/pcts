package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.memberrole.MemberRole;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.JwtService;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.persistence.MemberRolePersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MemberBusinessService extends BusinessBase<Member> {
    private final JwtService jwtService;
    private final MemberPersistenceService memberPersistenceService;
    private final RoleBusinessService roleBusinessService;
    private final CalculationBusinessService calculationBusinessService;
    private final MemberRolePersistenceService memberRolePersistenceService;

    public MemberBusinessService(MemberValidationService validationService,
                                 MemberPersistenceService memberPersistenceService,
                                 RoleBusinessService roleBusinessService,
                                 CalculationBusinessService calculationBusinessService, JwtService jwtService,
                                 MemberRolePersistenceService memberRolePersistenceService) {
        super(validationService, memberPersistenceService);
        this.jwtService = jwtService;
        this.roleBusinessService = roleBusinessService;
        this.calculationBusinessService = calculationBusinessService;
        this.memberPersistenceService = memberPersistenceService;
        this.memberRolePersistenceService = memberRolePersistenceService;
    }

    public Optional<Member> findIfExists(Long id) {
        return memberPersistenceService.findById(id);
    }

    public List<Member> getAll() {
        return persistenceService.getAll();
    }

    public Member getLoggedInMember() {
        return memberPersistenceService.getByLdapName(jwtService.getLdapName());
    }

    public List<Role> getAllRolesByMemberId(Long memberId) {
        Optional<List<MemberRole>> memberRoles = this.memberRolePersistenceService.findByMemberId(memberId);
        List<Role> roles = new ArrayList<>();
        if (memberRoles.isPresent()) {
            for (MemberRole memberRole : memberRoles.get()) {
                roles.add(roleBusinessService.getById(memberRole.getRoleId()));
            }
        }
        return roles;
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
        validationService.validateOnUpdate(id, member);

        Member oldEntry = getById(id);
        member.setId(id);
        member
                .keepSyncData(oldEntry.getPtimeId(),
                              oldEntry.getLastSuccessfulSync(),
                              oldEntry.getSyncErrorCount(),
                              oldEntry.getLdapName());

        return persistenceService.save(member);
    }

    @Transactional
    public void updateSyncMetadata(Long id, @Nullable Long ptimeId, @Nullable LocalDateTime lastSuccessfulSync,
                                   int syncErrorCount) {
        Member member = getById(id);

        if (ptimeId != null) {
            member.setPtimeId(ptimeId);
        }

        if (lastSuccessfulSync != null) {
            member.setLastSuccessfulSync(lastSuccessfulSync);
        }

        member.setSyncErrorCount(syncErrorCount);

        persistenceService.save(member);
    }

    public Optional<Member> findByPtimeId(Long id) {
        return memberPersistenceService.findByPtimeId(id);
    }

    public Optional<Member> findByAbbreviation(String abbreviation) {
        return memberPersistenceService.findByAbbreviation(abbreviation);
    }
}