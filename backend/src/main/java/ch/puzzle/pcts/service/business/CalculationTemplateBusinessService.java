package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.mapper.*;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CalculationTemplateBusinessService {
    private final MemberBusinessService memberBusinessService;
    private final DegreeBusinessService degreeBusinessService;
    private final CertificateBusinessService certificateBusinessService;
    private final LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    private final ExperienceBusinessService experienceBusinessService;
    private final RoleBusinessService roleBusinessService;
    private final DegreeMapper degreeMapper;
    private final ExperienceMapper experienceMapper;
    private final LeadershipExperienceMapper leadershipExperienceMapper;
    private final CertificateMapper certificateMapper;
    private final MemberMapper memberMapper;
    private final RoleMapper roleMapper;

    public CalculationTemplateBusinessService(MemberBusinessService memberBusinessService,
                                              DegreeBusinessService degreeBusinessService,
                                              CertificateBusinessService certificateBusinessService,
                                              LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                              ExperienceBusinessService experienceBusinessService,
                                              RoleBusinessService roleBusinessService, DegreeMapper degreeMapper,
                                              ExperienceMapper experienceMapper,
                                              LeadershipExperienceMapper leadershipExperienceMapper,
                                              CertificateMapper certificateMapper, MemberMapper memberMapper,
                                              RoleMapper roleMapper) {
        this.memberBusinessService = memberBusinessService;
        this.degreeBusinessService = degreeBusinessService;
        this.certificateBusinessService = certificateBusinessService;
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.experienceBusinessService = experienceBusinessService;
        this.roleBusinessService = roleBusinessService;
        this.degreeMapper = degreeMapper;
        this.experienceMapper = experienceMapper;
        this.leadershipExperienceMapper = leadershipExperienceMapper;
        this.certificateMapper = certificateMapper;
        this.memberMapper = memberMapper;
        this.roleMapper = roleMapper;
    }

    public CalculationDto getCalculationTemplate(Long memberId, Long roleId) {
        Member member = memberBusinessService.getById(memberId);
        Role role = roleBusinessService.getById(roleId);

        return getCalculationTemplateForMemberAndRole(member, role);
    }

    private CalculationDto getCalculationTemplateForMemberAndRole(Member member, Role role) {
        List<DegreeCalculationDto> degreeCalculations = degreeBusinessService
                .findAllByMember(member)
                .stream()
                .map(degree -> new DegreeCalculationDto(null, degreeMapper.toDto(degree), Map.of(), null))
                .toList();

        List<ExperienceCalculationDto> experienceCalculations = experienceBusinessService
                .findAllByMember(member)
                .stream()
                .map(experience -> new ExperienceCalculationDto(null,
                                                                experienceMapper.toDto(experience),
                                                                Relevancy.NONE,
                                                                null))
                .toList();

        List<LeadershipExperienceCalculationDto> leadershipCalculations = new ArrayList<>();

        if (role.getIsManagement()) {
            leadershipCalculations = leadershipExperienceBusinessService
                    .findAllByMember(member)
                    .stream()
                    .map(experience -> new LeadershipExperienceCalculationDto(null,
                                                                              leadershipExperienceMapper
                                                                                      .toDto(experience)))
                    .toList();
        }

        List<CertificateCalculationDto> certificateCalculations = certificateBusinessService
                .findAllByMember(member)
                .stream()
                .map(certificate -> new CertificateCalculationDto(null, certificateMapper.toDto(certificate)))
                .toList();

        BigDecimal points = BigDecimal.ZERO;
        points = points.add(getLeadershipExperiencePoints(leadershipCalculations));
        points = points.add(getCertificatePoints(certificateCalculations));

        return new CalculationDto(null,
                                  memberMapper.toDto(member),
                                  roleMapper.toDto(role),
                                  CalculationState.DRAFT,
                                  null,
                                  null,
                                  points,
                                  certificateCalculations,
                                  leadershipCalculations,
                                  degreeCalculations,
                                  experienceCalculations);
    }

    private BigDecimal getLeadershipExperiencePoints(List<LeadershipExperienceCalculationDto> leadershipExperienceCalculationDtos) {
        return leadershipExperienceCalculationDtos
                .stream()
                .map(dto -> dto.leadershipExperience().leadershipExperienceType().points())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getCertificatePoints(List<CertificateCalculationDto> certificateCalculationDtos) {
        return certificateCalculationDtos
                .stream()
                .map(dto -> dto.certificate().certificateType().points())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
