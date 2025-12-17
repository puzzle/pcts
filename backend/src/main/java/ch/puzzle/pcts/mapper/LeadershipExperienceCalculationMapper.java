package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceCalculationMapper {

    private final LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    private final LeadershipExperienceMapper leadershipExperienceMapper;

    public LeadershipExperienceCalculationMapper(LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                                 LeadershipExperienceMapper leadershipExperienceMapper) {
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.leadershipExperienceMapper = leadershipExperienceMapper;

    }

    public List<LeadershipExperienceCalculationDto> toDto(List<CertificateCalculation> models) {
        return models.stream().map(this::toDto).filter(Objects::nonNull).toList();
    }

    public List<CertificateCalculation> fromDto(List<Long> ids) {
        return ids.stream().map(this::fromDto).toList();
    }

    public CertificateCalculation fromDto(Long id) {
        return new CertificateCalculation(null, null, leadershipExperienceBusinessService.getById(id));
    }

    public LeadershipExperienceCalculationDto toDto(CertificateCalculation model) {
        if (model.getCertificate().getCertificateType().getCertificateKind() == CertificateKind.CERTIFICATE) {
            return null;
        }
        return new LeadershipExperienceCalculationDto(model.getId(),
                                                      leadershipExperienceMapper.toDto(model.getCertificate()));
    }
}
