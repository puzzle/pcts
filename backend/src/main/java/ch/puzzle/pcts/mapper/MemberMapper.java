package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.model.member.Member;
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
                             model.getName(),
                             model.getLastName(),
                             model.getEmploymentState(),
                             model.getAbbreviation(),
                             model.getDateOfHire(),
                             model.getBirthDate(),
                             model.isAdmin(),
                             organisationUnitMapper.toDto(model.getOrganisationUnit()));
    }

    public Member fromDto(MemberInputDto dto) {
        return new Member(dto.id(),
                          dto.name(),
                          dto.lastName(),
                          dto.employmentState(),
                          dto.abbreviation(),
                          dto.dateOfHire(),
                          dto.birthDate(),
                          dto.isAdmin(),
                          organisationUnitBusinessService.getById(dto.organisationUnitId()));
    }
}
