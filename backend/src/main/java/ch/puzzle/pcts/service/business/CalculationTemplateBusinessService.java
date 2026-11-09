package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationTemplateBusinessService {
    private final MemberBusinessService memberBusinessService;
    private final DegreeBusinessService degreeBusinessService;
    private final CertificateBusinessService certificateBusinessService;
    private final LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    private final ExperienceBusinessService experienceBusinessService;
    private final RoleBusinessService roleBusinessService;

    public CalculationTemplateBusinessService(MemberBusinessService memberBusinessService,
                                              DegreeBusinessService degreeBusinessService,
                                              CertificateBusinessService certificateBusinessService,
                                              LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                              ExperienceBusinessService experienceBusinessService,
                                              RoleBusinessService roleBusinessService) {
        this.memberBusinessService = memberBusinessService;
        this.degreeBusinessService = degreeBusinessService;
        this.certificateBusinessService = certificateBusinessService;
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.experienceBusinessService = experienceBusinessService;
        this.roleBusinessService = roleBusinessService;
    }

    public Calculation getCalculationTemplate(Long memberId, Long roleId) {
        Member member = memberBusinessService.getById(memberId);
        Role role = roleBusinessService.getById(roleId);

        return getCalculationTemplateForMemberAndRole(member, role);
    }

    private Calculation getCalculationTemplateForMemberAndRole(Member member, Role role) {
        List<DegreeCalculation> degreeCalculations = degreeBusinessService
                .findAllByMember(member)
                .stream()
                .map(degree -> DegreeCalculation.Builder.builder().withDegree(degree).build())
                .toList();

        List<ExperienceCalculation> experienceCalculations = experienceBusinessService
                .findAllByMember(member)
                .stream()
                .map(experience -> ExperienceCalculation.Builder.builder().withExperience(experience).build())
                .toList();

        List<LeadershipExperienceCalculation> leadershipCalculations = new ArrayList<>();

        if (role.getIsManagement()) {
            leadershipCalculations = leadershipExperienceBusinessService
                    .findAllByMember(member)
                    .stream()
                    .map(leadershipExperience -> LeadershipExperienceCalculation.Builder
                            .builder()
                            .withLeadershipExperience(leadershipExperience)
                            .build())
                    .toList();
        }

        List<CertificateCalculation> certificateCalculations = certificateBusinessService
                .findAllByMember(member)
                .stream()
                .map(certificate -> CertificateCalculation.Builder.builder().withCertificate(certificate).build())
                .toList();

        return Calculation.Builder
                .builder()
                .withId(null)
                .withMember(member)
                .withRole(role)
                .withState(CalculationState.DRAFT)
                .withPublicationDate(null)
                .withPublicizedBy(null)
                .withPoints(BigDecimal.ZERO)
                .withCertificateCalculations(certificateCalculations)
                .withLeadershipExperienceCalculations(leadershipCalculations)
                .withDegreeCalculations(degreeCalculations)
                .withExperienceCalculations(experienceCalculations)
                .build();
    }
}
