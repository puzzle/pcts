package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DegreeMapper {
    MemberBusinessService memberBusinessService;
    DegreeTypeBusinessService degreeTypeBusinessService;
    DegreeTypeMapper degreeTypeMapper;
    MemberMapper memberMapper;

    DegreeMapper(MemberBusinessService memberBusinessService, DegreeTypeBusinessService degreeTypeBusinessService,
                 DegreeTypeMapper degreeTypeMapper, MemberMapper memberMapper) {
        this.memberBusinessService = memberBusinessService;
        this.degreeTypeBusinessService = degreeTypeBusinessService;
        this.degreeTypeMapper = degreeTypeMapper;
        this.memberMapper = memberMapper;
    }

    public List<DegreeDto> toDto(List<Degree> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Degree> fromDto(List<DegreeInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public DegreeDto toDto(Degree model) {
        return new DegreeDto(model.getId(),
                             this.memberMapper.toDto(model.getMember()),
                             model.getName(),
                             model.getInstitution(),
                             model.getCompleted(),
                             this.degreeTypeMapper.toDto(model.getDegreeType()),
                             model.getStartDate(),
                             model.getEndDate(),
                             model.getComment());
    }

    public Degree fromDto(DegreeInputDto dto) {
        return Degree.Builder
                .builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withName(dto.name())
                .withInstitution(dto.institution())
                .withCompleted(dto.completed())
                .withDegreeType(degreeTypeBusinessService.getById(dto.typeId()))
                .withStartDate(dto.startDate())
                .withEndDate(dto.endDate())
                .withComment(dto.comment())
                .build();
    }
}
