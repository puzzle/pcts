package ch.puzzle.pcts.dto.memberoverview;

import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.leadershipexperience.MemberOverviewLeadershipExperienceDto;
import java.util.List;

public record MemberCvDto(List<MemberOverviewDegreeDto> degrees, List<MemberOverviewExperienceDto> experiences,
        List<MemberOverviewCertificateDto> certificates,
        List<MemberOverviewLeadershipExperienceDto> leadershipExperiences) {
}
