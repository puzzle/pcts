package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {
    private final MemberMapper memberMapper;
    private final ExperienceTypeMapper experienceTypeMapper;
    private final MemberBusinessService memberBusinessService;
    private final ExperienceTypeBusinessService experienceTypeBusinessService;

    public ExperienceMapper(MemberMapper memberMapper, ExperienceTypeMapper experienceTypeMapper,
                            MemberBusinessService memberBusinessService,
                            ExperienceTypeBusinessService experienceTypeBusinessService) {
        this.memberMapper = memberMapper;
        this.experienceTypeMapper = experienceTypeMapper;
        this.memberBusinessService = memberBusinessService;
        this.experienceTypeBusinessService = experienceTypeBusinessService;
    }

    public List<ExperienceDto> toDto(List<Experience> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Experience> fromDto(List<ExperienceInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public ExperienceDto toDto(Experience model) {
        return new ExperienceDto(model.getId(),
                                 memberMapper.toDto(model.getMember()),
                                 model.getName(),
                                 model.getEmployer(),
                                 model.getPercent(),
                                 experienceTypeMapper.toDto(model.getType()),
                                 model.getComment(),
                                 model.getStartDate(),
                                 model.getEndDate());
    }

    public Experience fromDto(ExperienceInputDto dto) {
        return new Experience.Builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withName(dto.name())
                .withEmployer(dto.employer())
                .withPercent(dto.percent())
                .withType(experienceTypeBusinessService.getById(dto.experienceTypeId()))
                .withComment(dto.comment())
                .withStartDate(dto.startDate())
                .withEndDate(dto.endDate())
                .build();
    }
}