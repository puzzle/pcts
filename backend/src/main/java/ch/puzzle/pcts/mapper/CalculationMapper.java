package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class CalculationMapper {

    private final MemberMapper memberMapper;
    private final MemberBusinessService memberBusinessService;
    private final RoleMapper roleMapper;
    private final RoleBusinessService roleBusinessService;
    private final ExperienceCalculationMapper experienceCalculationMapper;
    private final DegreeCalculationMapper degreeCalculationMapper;
    private final CertificateCalculationMapper certificateCalculationMapper;
    private final LeadershipExperienceCalculationMapper leadershipExperienceCalculationMapper;

    public CalculationMapper(MemberMapper memberMapper, MemberBusinessService memberBusinessService,
                             RoleMapper roleMapper, RoleBusinessService roleBusinessService,
                             ExperienceCalculationMapper experienceCalculationMapper,
                             DegreeCalculationMapper degreeCalculationMapper,
                             CertificateCalculationMapper certificateCalculationMapper,
                             LeadershipExperienceCalculationMapper leadershipExperienceCalculationMapper) {
        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.roleMapper = roleMapper;
        this.roleBusinessService = roleBusinessService;
        this.experienceCalculationMapper = experienceCalculationMapper;
        this.degreeCalculationMapper = degreeCalculationMapper;
        this.certificateCalculationMapper = certificateCalculationMapper;
        this.leadershipExperienceCalculationMapper = leadershipExperienceCalculationMapper;
    }

    public List<CalculationDto> toDto(List<Calculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Calculation> fromDto(List<CalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public CalculationDto toDto(Calculation model) {
        return new CalculationDto(model.getId(),
                                  memberMapper.toDto(model.getMember()),
                                  roleMapper.toDto(model.getRole()),
                                  model.getState(),
                                  model.getPublicationDate(),
                                  model.getPublicizedBy(),
                                  model.getPoints(),
                                  certificateCalculationMapper.toDto(model.getCertificates()),
                                  leadershipExperienceCalculationMapper.toDto(model.getCertificates()),
                                  degreeCalculationMapper.toDto(model.getDegrees()),
                                  experienceCalculationMapper.toDto(model.getExperiences()));
    }

    public Calculation fromDto(CalculationInputDto dto) {
        return Calculation.Builder
                .builder()
                .withMember(this.memberBusinessService.getById(dto.memberId()))
                .withRole(this.roleBusinessService.getById(dto.roleId()))
                .withState(dto.state())
                .withPublicationDate(null)
                .withPublicizedBy(null)
                .withDegrees(this.degreeCalculationMapper.fromDto(dto.degrees()))
                .withExperiences(this.experienceCalculationMapper.fromDto(dto.experiences()))
                .withCertificates(mergeCertificates(dto.leadershipExperiences(), dto.certificates()))
                .build();
    }

    private List<CertificateCalculation> mergeCertificates(List<LeadershipExperienceCalculationInputDto> leadershipExperiences,
                                                           List<CertificateCalculationInputDto> certificates) {
        return Stream
                .concat(certificateCalculationMapper.fromDto(certificates).stream(),
                        leadershipExperienceCalculationMapper.fromDto(leadershipExperiences).stream())
                .toList();
    }
}
