package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    OrganisationUnitBusinessService organisationUnitBusinessService;
    OrganisationUnitMapper organisationUnitMapper;

    public MemberMapper(OrganisationUnitBusinessService organisationUnitBusinessService,
                        OrganisationUnitMapper organisationUnitMapper) {
        this.organisationUnitBusinessService = organisationUnitBusinessService;
        this.organisationUnitMapper = organisationUnitMapper;
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
                             organisationUnitMapper.toDto(model.getOrganisationUnit()));
    }

    public Member fromDto(MemberInputDto dto) {
        return Member.Builder
                .builder()
                .withFirstName(dto.firstName())
                .withLastName(dto.lastName())
                .withEmploymentState(dto.employmentState())
                .withAbbreviation(dto.abbreviation())
                .withDateOfHire(dto.dateOfHire())
                .withBirthDate(dto.birthDate())
                .withOrganisationUnit(organisationUnitFromId(dto.organisationUnitId()))
                .build();
    }

    protected OrganisationUnit organisationUnitFromId(Long organisationUnitId) {
        return organisationUnitId == null ? null : organisationUnitBusinessService.getById(organisationUnitId);
    }
}
