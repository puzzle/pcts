package ch.puzzle.pcts.dto.memberoverview;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import java.util.List;

public record MemberCvDto(List<DegreeDto> degrees, List<ExperienceDto> experiences, List<CertificateDto> certificates,
        List<LeadershipExperienceDto> leadershipExperiences) {
}
