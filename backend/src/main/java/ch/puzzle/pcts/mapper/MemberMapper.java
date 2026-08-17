package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.ArrayList;
import java.util.List;
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
        // return dtos.stream().map(this::fromDto).toList();
        return List.of();
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

    protected List<Role> rolesFromIds(List<Long> roleIds) {
        List<Role> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            roles.add(roleBusinessService.getById(roleId));
        }
        return roles;
    }
}
