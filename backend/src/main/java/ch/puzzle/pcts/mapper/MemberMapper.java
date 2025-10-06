package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    OrganisationUnitBusinessService organisationUnitBusinessService;
    public MemberMapper(OrganisationUnitBusinessService organisationUnitBusinessService) {
        this.organisationUnitBusinessService = organisationUnitBusinessService;
    }
    public List<MemberDto> toDto(List<Member> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Member> fromDto(List<MemberDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public MemberDto toDto(Member model) {
        return new MemberDto(model.getId(),
                             model.getName(),
                             model.getLastName(),
                             model.getEmploymentState(),
                             model.getAbbreviation(),
                             model.getDateOfHire(),
                             model.isAdmin());
    }

    public Member fromDto(MemberDto dto) {
        return new Member(dto.id(),
                          dto.name(),
                          dto.lastName(),
                          dto.employmentState(),
                          dto.abbreviation(),
                          dto.dateOfHire(),
                          dto.isAdmin(),
                          organisationUnitBusinessService.getById(1));
    }
}
