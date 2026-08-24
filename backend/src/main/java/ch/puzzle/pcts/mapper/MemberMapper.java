package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    private final RoleBusinessService roleBusinessService;
    OrganisationUnitBusinessService organisationUnitBusinessService;
    OrganisationUnitMapper organisationUnitMapper;

    public MemberMapper(OrganisationUnitBusinessService organisationUnitBusinessService,
                        OrganisationUnitMapper organisationUnitMapper, RoleBusinessService roleBusinessService) {
        this.organisationUnitBusinessService = organisationUnitBusinessService;
        this.organisationUnitMapper = organisationUnitMapper;
        this.roleBusinessService = roleBusinessService;
    }

    public List<MemberDto> toDto(List<Member> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Member> fromDto(List<MemberInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public MemberDto toDto(Member model) {

        return new MemberDto(model.getId(),
                             model.getFirstName(),
                             model.getLastName(),
                             model.getEmploymentState(),
                             model.getAbbreviation(),
                             model.getDateOfHire(),
                             model.getBirthDate(),
                             organisationUnitMapper.toDto(model.getOrganisationUnit()),
                             model.getRoles(),
                             model.getPtimeId(),
                             model.getLastSuccessfulSync(),
                             model.getSyncErrorCount());
    }

    public Member fromDto(MemberInputDto dto) {
        return Member.Builder
                .builder()
                .withFirstName(dto.firstName())
                .withLastName(dto.lastName())
                .withEmploymentState(dto.employmentState())
                .withAbbreviation(dto.abbreviation())
                .withRoles(rolesFromIds(dto.roleIds()))
                .withDateOfHire(dto.dateOfHire())
                .withBirthDate(dto.birthDate())
                .withOrganisationUnit(organisationUnitFromId(dto.organisationUnitId()))
                .build();
    }

    protected OrganisationUnit organisationUnitFromId(Long organisationUnitId) {
        return organisationUnitId == null ? null : organisationUnitBusinessService.getById(organisationUnitId);
    }

    private Set<Role> rolesFromIds(Set<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        roleIds.stream().forEach(roleId -> roles.add(roleBusinessService.getById(roleId)));
        return roles;
    }
}
