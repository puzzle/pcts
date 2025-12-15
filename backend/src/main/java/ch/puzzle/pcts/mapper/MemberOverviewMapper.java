package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.memberoverview.MemberCvDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberOverviewMapper {

    public MemberOverviewMapper() {
    }

    public MemberOverviewDto toDto(List<MemberOverview> entities) {

        MemberOverview first = entities.getFirst();

        MemberDto memberDto = new MemberDto(first.getId(),
                                            first.getFirstName(),
                                            first.getLastName(),
                                            first.getEmploymentState(),
                                            first.getAbbreviation(),
                                            first.getDateOfHire(),
                                            first.getBirthDate(),
                                            new OrganisationUnitDto(first.getOrganisationUnitId(),
                                                                    first.getOrganisationUnitName()));

        List<DegreeDto> degrees = entities
                .stream()
                .filter(e -> e.getDegreeId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getDegreeId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();

                    return new DegreeDto(e.getDegreeId(),
                                         memberDto,
                                         e.getDegreeName(),
                                         e.getDegreeInstitution(),
                                         e.getDegreeCompleted(),
                                         new DegreeTypeDto(e.getDegreeTypeId(),
                                                           e.getDegreeTypeName(),
                                                           e.getDegreeHighlyRelevantPoints(),
                                                           e.getDegreeLimitedRelevantPoints(),
                                                           e.getDegreeLittleRelevantPoints()),
                                         e.getDegreeStartDate(),
                                         e.getDegreeEndDate(),
                                         e.getDegreeComment());
                })
                .toList();

        List<ExperienceDto> experiences = entities
                .stream()
                .filter(e -> e.getExperienceId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getExperienceId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();

                    return new ExperienceDto(e.getExperienceId(),
                                             memberDto,
                                             e.getExperienceName(),
                                             e.getExperienceEmployer(),
                                             e.getExperiencePercent(),
                                             new ExperienceTypeDto(e.getExperienceTypeId(),
                                                                   e.getExperienceTypeName(),
                                                                   e.getExperienceHighlyRelevantPoints(),
                                                                   e.getExperienceLimitedRelevantPoints(),
                                                                   e.getExperienceLittleRelevantPoints()),
                                             e.getExperienceComment(),
                                             e.getExperienceStartDate(),
                                             e.getExperienceEndDate());
                })
                .toList();

        Map<Long, List<MemberOverview>> certificateGroups = entities
                .stream()
                .filter(e -> e.getCertificateId() != null && "CERTIFICATE".equals(e.getCertificateKind()))
                .collect(Collectors.groupingBy(MemberOverview::getCertificateId));

        List<CertificateDto> certificates = certificateGroups.values().stream().map(certRows -> {

            MemberOverview certData = certRows.get(0);

            List<String> tags = certRows
                    .stream()
                    .map(MemberOverview::getTagName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();

            return new CertificateDto(certData.getCertificateId(),
                                      memberDto,
                                      new CertificateTypeDto(certData.getCertificateTypeId(),
                                                             certData.getCertificateTypeName(),
                                                             certData.getCertificateTypePoints(),
                                                             certData.getCertificateTypeComment(),
                                                             tags),
                                      certData.getCertificateCompletedAt(),
                                      certData.getCertificateValidUntil(),
                                      certData.getCertificateComment());
        }).toList();

        List<LeadershipExperienceDto> leadershipExperiences = entities
                .stream()
                .filter(e -> e.getLeadershipId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getLeadershipId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();

                    return new LeadershipExperienceDto(e.getLeadershipId(),
                                                       memberDto,
                                                       new LeadershipExperienceTypeDto(e.getLeadershipTypeId(),
                                                                                       e.getLeadershipTypeName(),
                                                                                       e.getLeadershipTypePoints(),
                                                                                       e.getLeadershipTypeComment(),
                                                                                       e.getLeadershipKind()),
                                                       e.getLeadershipComment());
                })
                .toList();

        List<CalculationDto> calculations = entities
                .stream()
                .filter(e -> e.getCalculationId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getCalculationId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();

                    return new CalculationDto(e.getCalculationId(),
                                              memberDto,
                                              new RoleDto(e.getRoleId(), e.getRoleName(), e.getRoleIsManagement()),
                                              e.getCalculationState(),
                                              e.getCalculationPublicationDate(),
                                              e.getCalculationPublicizedBy());
                })
                .toList();

        MemberCvDto cvDto = new MemberCvDto(degrees, experiences, certificates, leadershipExperiences);

        return new MemberOverviewDto(memberDto, cvDto, calculations);
    }
}
