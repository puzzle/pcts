package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
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

    public CalculationMapper(MemberMapper memberMapper, MemberBusinessService memberBusinessService,
                             RoleMapper roleMapper, RoleBusinessService roleBusinessService,
                             ExperienceCalculationMapper experienceCalculationMapper,
                             DegreeCalculationMapper degreeCalculationMapper,
                             CertificateCalculationMapper certificateCalculationMapper) {
        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.roleMapper = roleMapper;
        this.roleBusinessService = roleBusinessService;
        this.experienceCalculationMapper = experienceCalculationMapper;
        this.degreeCalculationMapper = degreeCalculationMapper;
        this.certificateCalculationMapper = certificateCalculationMapper;
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
                                  model.getPublicizedBy());
    }

    public Calculation fromDto(CalculationInputDto dto) {
        return new Calculation(null,
                               this.memberBusinessService.getById(dto.memberId()),
                               this.roleBusinessService.getById(dto.roleId()),
                               dto.state(),
                               null,
                               null,
                               this.degreeCalculationMapper.fromDto(dto.degrees()),
                               this.experienceCalculationMapper.fromDto(dto.experiences()),
                               mergedCertificates(dto.leadershipExperiences(), dto.certificates()));
    }

    private List<CertificateCalculation> mergedCertificates(List<Long> leadershipExperiences, List<Long> certificates) {
        return Stream
                .of(certificates, leadershipExperiences)
                .map(certificateCalculationMapper::fromDto)
                .flatMap(List::stream)
                .toList();
    }
}
