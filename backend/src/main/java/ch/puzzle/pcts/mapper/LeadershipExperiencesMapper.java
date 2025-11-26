package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.leadershipExperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipExperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperiencesMapper {
    private final LeadershipExperienceTypeMapper leadershipExperienceTypeMapper;
    private final LeadershipExperienceTypeBusinessService leadershipExperienceTypeBusinessService;

    public LeadershipExperiencesMapper(LeadershipExperienceTypeMapper leadershipExperienceTypeMapper,
                                       LeadershipExperienceTypeBusinessService leadershipExperiencesBusinessServiceder) {
        this.leadershipExperienceTypeMapper = leadershipExperienceTypeMapper;
        this.leadershipExperienceTypeBusinessService = leadershipExperiencesBusinessServiceder;
    }

    public List<LeadershipExperienceDto> toDto(List<Certificate> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Certificate> fromDto(List<LeadershipExperienceInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceDto toDto(Certificate model) {
        return new LeadershipExperienceDto(model.getId(),
                                           model.getComment(),
                                           leadershipExperienceTypeMapper.toDto(model.getCertificateType()));
    }

    public Certificate fromDto(LeadershipExperienceInputDto dto) {
        return Certificate.Builder
                .builder()
                .withCertificateType(this.leadershipExperienceTypeBusinessService
                        .getById(dto.leadershipExperienceTypeId()))
                .withId(dto.id())
                .withComment(dto.comment())
                .build();
    }
}
