package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.memberoverview.*;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateTypeDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeTypeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceTypeDto;
import ch.puzzle.pcts.dto.memberoverview.leadershipexperience.MemberOverviewLeadershipExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.leadershipexperience.MemberOverviewLeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberOverviewMapper {

    public MemberOverviewDto toDto(List<MemberOverview> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        MemberOverview first = entities.getFirst();

        MemberOverviewMemberDto memberDto = new MemberOverviewMemberDto(first.getMemberId(),
                                                                        first.getFirstName(),
                                                                        first.getLastName(),
                                                                        first.getEmploymentState(),
                                                                        first.getAbbreviation(),
                                                                        first.getDateOfHire(),
                                                                        first.getBirthDate(),
                                                                        first.getOrganisationUnitName());

        List<MemberOverviewDegreeDto> degrees = entities
                .stream()
                .filter(e -> e.getDegreeId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getDegreeId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();
                    return new MemberOverviewDegreeDto(e.getDegreeId(),
                                                       e.getDegreeName(),
                                                       new MemberOverviewDegreeTypeDto(e.getDegreeTypeName()),
                                                       e.getDegreeStartDate(),
                                                       e.getDegreeEndDate(),
                                                       e.getDegreeComment());
                })
                .toList();

        List<MemberOverviewExperienceDto> experiences = entities
                .stream()
                .filter(e -> e.getExperienceId() != null)
                .collect(Collectors.groupingBy(MemberOverview::getExperienceId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();
                    return new MemberOverviewExperienceDto(e.getExperienceId(),
                                                           e.getExperienceName(),
                                                           e.getExperienceEmployer(),
                                                           new MemberOverviewExperienceTypeDto(e
                                                                   .getExperienceTypeName()),
                                                           e.getExperienceComment(),
                                                           e.getExperienceStartDate(),
                                                           e.getExperienceEndDate());
                })
                .toList();

        List<MemberOverviewCertificateDto> certificates = entities
                .stream()
                .filter(e -> e.getCertificateId() != null
                             && CertificateKind.CERTIFICATE.equals(e.getLeadershipTypeKind()))
                .collect(Collectors.groupingBy(MemberOverview::getCertificateId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();
                    return new MemberOverviewCertificateDto(e.getCertificateId(),
                                                            new MemberOverviewCertificateTypeDto(e
                                                                    .getCertificateTypeName(),
                                                                                                 e
                                                                                                         .getCertificateTypeComment()),
                                                            e.getCertificateCompletedAt(),
                                                            e.getCertificateValidUntil(),
                                                            e.getCertificateComment());
                })
                .toList();

        List<MemberOverviewLeadershipExperienceDto> LeadershipExperiences = entities
                .stream()
                .filter(e -> e.getCertificateId() != null
                             && !CertificateKind.CERTIFICATE.equals(e.getLeadershipTypeKind()))
                .collect(Collectors.groupingBy(MemberOverview::getCertificateId))
                .values()
                .stream()
                .map(group -> {
                    MemberOverview e = group.getFirst();
                    return new MemberOverviewLeadershipExperienceDto(e.getCertificateId(),
                                                                     new MemberOverviewLeadershipExperienceTypeDto(e
                                                                             .getCertificateTypeName(),
                                                                                                                   e
                                                                                                                           .getCertificateTypeComment(),
                                                                                                                   e
                                                                                                                           .getLeadershipTypeKind()),
                                                                     e.getCertificateComment());
                })
                .toList();

        MemberCvDto cvDto = new MemberCvDto(degrees, experiences, certificates, LeadershipExperiences);

        return new MemberOverviewDto(memberDto, cvDto);
    }
}
