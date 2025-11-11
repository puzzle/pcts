package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceTypeMapper {

    public List<LeadershipExperienceTypeDto> toDto(List<CertificateType> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<CertificateType> fromDto(List<LeadershipExperienceTypeDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceTypeDto toDto(CertificateType model) {
        return new LeadershipExperienceTypeDto(model
                .getId(), model.getName(), model.getPoints(), model.getComment(), model.getCertificateKind());

    }

    public CertificateType fromDto(LeadershipExperienceTypeDto dto) {
        return new CertificateType(dto.id(), dto.name(), dto.points(), dto.comment(), dto.certificateKind());
    }
}
