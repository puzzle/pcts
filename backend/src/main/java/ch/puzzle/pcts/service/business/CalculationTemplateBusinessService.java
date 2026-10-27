package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.member.Member;
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

    public CalculationTemplateBusinessService(MemberBusinessService memberBusinessService,
                                              DegreeBusinessService degreeBusinessService,
                                              CertificateBusinessService certificateBusinessService,
                                              LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                              ExperienceBusinessService experienceBusinessService) {
        this.memberBusinessService = memberBusinessService;
        this.degreeBusinessService = degreeBusinessService;
        this.certificateBusinessService = certificateBusinessService;
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.experienceBusinessService = experienceBusinessService;
    }

    public CalculationInputDto getCalculationTemplate(Long memberId) {
        Member member = memberBusinessService.getById(memberId);

        return getCalculationTemplateDtoForMember(member);
    }

    private CalculationInputDto getCalculationTemplateDtoForMember(Member member) {
        List<DegreeCalculationInputDto> degreeCalculations = degreeBusinessService
                .findAllByMember(member)
                .stream()
                .map(degree -> new DegreeCalculationInputDto(null, degree.getId(), Map.of(), null))
                .toList();

        List<ExperienceCalculationInputDto> experienceCalculations = experienceBusinessService
                .findAllByMember(member)
                .stream()
                .map(experience -> new ExperienceCalculationInputDto(null, experience.getId(), Relevancy.NONE, null))
                .toList();

        List<LeadershipExperienceCalculationInputDto> leadershipCalculations = leadershipExperienceBusinessService
                .findAllByMember(member)
                .stream()
                .map(experience -> new LeadershipExperienceCalculationInputDto(null, experience.getId()))
                .toList();

        List<CertificateCalculationInputDto> certificateCalculations = certificateBusinessService
                .findAllByMember(member)
                .stream()
                .map(certificate -> new CertificateCalculationInputDto(null, certificate.getId()))
                .toList();

        return new CalculationInputDto(member.getId(),
                                       CalculationState.DRAFT,
                                       null,
                                       certificateCalculations,
                                       leadershipCalculations,
                                       degreeCalculations,
                                       experienceCalculations);
    }
}
