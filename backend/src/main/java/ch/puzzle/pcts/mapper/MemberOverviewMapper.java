package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.memberoverview.*;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.leadershipexperience.MemberOverviewLeadershipExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.leadershipexperience.MemberOverviewLeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MemberOverviewMapper {

    public MemberOverviewDto toDto(List<MemberOverview> memberOverviews) {
        if (memberOverviews == null || memberOverviews.isEmpty()) {
            return null;
        }

        Map<Long, MemberOverviewDegreeDto> degreeMap = new HashMap<>();
        Map<Long, MemberOverviewExperienceDto> experienceMap = new HashMap<>();
        Map<Long, MemberOverviewCertificateDto> certificateMap = new HashMap<>();
        Map<Long, MemberOverviewLeadershipExperienceDto> leadershipMap = new HashMap<>();

        for (MemberOverview row : memberOverviews) {
            handleDegree(row, degreeMap);
            handleExperience(row, experienceMap);
            handleCertificate(row, certificateMap, leadershipMap);
        }

        MemberOverviewCvDto cvDto = new MemberOverviewCvDto(List.copyOf(degreeMap.values()),
                                                            List.copyOf(experienceMap.values()),
                                                            List.copyOf(certificateMap.values()),
                                                            List.copyOf(leadershipMap.values()));

        return new MemberOverviewDto(getMemberDto(memberOverviews), cvDto);
    }

    private MemberOverviewMemberDto getMemberDto(List<MemberOverview> memberOverviews) {
        MemberOverview first = memberOverviews.getFirst();
        return new MemberOverviewMemberDto(first.getMemberId(),
                                           first.getFirstName(),
                                           first.getLastName(),
                                           first.getEmploymentState(),
                                           first.getAbbreviation(),
                                           first.getDateOfHire(),
                                           first.getBirthDate(),
                                           first.getOrganisationUnitName());
    }

    private MemberOverviewDegreeDto mapToDegree(MemberOverview e) {
        return new MemberOverviewDegreeDto(e
                .getDegreeId(), e.getDegreeName(), e.getDegreeTypeName(), e.getDegreeStartDate(), e.getDegreeEndDate());
    }

    private MemberOverviewExperienceDto mapToExperience(MemberOverview e) {
        return new MemberOverviewExperienceDto(e.getExperienceId(),
                                               e.getExperienceName(),
                                               e.getExperienceEmployer(),
                                               e.getExperienceTypeName(),
                                               e.getExperienceComment(),
                                               e.getExperienceStartDate(),
                                               e.getExperienceEndDate());
    }

    private MemberOverviewCertificateDto mapToCertificate(MemberOverview e) {
        return new MemberOverviewCertificateDto(e.getCertificateId(),
                                                e.getCertificateTypeName(),
                                                e.getCertificateCompletedAt(),
                                                e.getCertificateComment());
    }

    private MemberOverviewLeadershipExperienceDto mapToLeadershipExperience(MemberOverview e) {
        return new MemberOverviewLeadershipExperienceDto(e.getCertificateId(),
                                                         new MemberOverviewLeadershipExperienceTypeDto(e
                                                                 .getCertificateTypeName(), e.getLeadershipTypeKind()),
                                                         e.getCertificateComment());
    }

    private void handleDegree(MemberOverview row, Map<Long, MemberOverviewDegreeDto> degreeMap) {
        if (!hasValidId(row.getDegreeId())) {
            return;
        }
        degreeMap.putIfAbsent(row.getDegreeId(), mapToDegree(row));
    }

    private void handleExperience(MemberOverview row, Map<Long, MemberOverviewExperienceDto> experienceMap) {
        if (!hasValidId(row.getExperienceId())) {
            return;
        }
        experienceMap.putIfAbsent(row.getExperienceId(), mapToExperience(row));
    }

    private void handleCertificate(MemberOverview row, Map<Long, MemberOverviewCertificateDto> certificateMap,
                                   Map<Long, MemberOverviewLeadershipExperienceDto> leadershipMap) {

        if (!hasValidId(row.getCertificateId())) {
            return;
        }

        if (row.getLeadershipTypeKind().isLeadershipExperienceType()) {
            leadershipMap.putIfAbsent(row.getCertificateId(), mapToLeadershipExperience(row));
        } else {
            certificateMap.putIfAbsent(row.getCertificateId(), mapToCertificate(row));
        }
    }

    private boolean hasValidId(Long id) {
        return id != null && id > 0;
    }
}