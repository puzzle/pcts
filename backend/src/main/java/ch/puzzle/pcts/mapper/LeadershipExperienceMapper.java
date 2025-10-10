package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.leadership_experience.LeadershipExperienceDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceMapper {

    public List<LeadershipExperienceDto> toDto(List<Certificate> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Certificate> fromDto(List<LeadershipExperienceDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceDto toDto(Certificate model) {
        return new LeadershipExperienceDto(model
                .getId(), model.getName(), model.getPoints(), model.getComment(), model.getCertificateType());

    }

    public Certificate fromDto(LeadershipExperienceDto dto) {
        return new Certificate(dto.id(), dto.name(), dto.points(), dto.comment(), dto.certificateType());
    }
}
