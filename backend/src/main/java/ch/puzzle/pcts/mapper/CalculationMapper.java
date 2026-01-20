package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.ArrayList;
import java.util.List;
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
                                  certificateCalculationMapper
                                          .toDto(model.getCertificateCalculationsWithCertificateType()),
                                  leadershipExperienceCalculationMapper
                                          .toDto(model.getCertificatesCalculationsWithLeadershipExperienceType()),
                                  degreeCalculationMapper.toDto(model.getDegreeCalculations()),
                                  experienceCalculationMapper.toDto(model.getExperienceCalculations()));
    }

    public Calculation fromDto(CalculationInputDto dto) {
        return Calculation.Builder
                .builder()
                .withMember(this.memberBusinessService.getById(dto.memberId()))
                .withRole(this.roleBusinessService.getById(dto.roleId()))
                .withState(dto.state())
                .withPublicationDate(null)
                .withPublicizedBy(null)
                .withDegreeCalculations(this.degreeCalculationMapper.fromDto(dto.degreeCalculations()))
                .withExperienceCalculations(this.experienceCalculationMapper.fromDto(dto.experienceCalculations()))
                .withCertificateCalculations(mergeCertificates(dto.leadershipExperienceCalculations(),
                                                               dto.certificateCalculations()))
                .build();
    }

    private List<CertificateCalculation> mergeCertificates(List<LeadershipExperienceCalculationInputDto> leadershipExperiences,
                                                           List<CertificateCalculationInputDto> certificates) {
        List<CertificateCalculation> certificateCalculations = new ArrayList<>();
        certificateCalculations.addAll(certificateCalculationMapper.fromDto(certificates));
        certificateCalculations.addAll(leadershipExperienceCalculationMapper.fromDto(leadershipExperiences));
        return certificateCalculations;
    }
}
