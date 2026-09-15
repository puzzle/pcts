package ch.puzzle.pctsmigration.leadershipexperience;

import java.time.LocalDate;
import java.util.List;
import org.openapitools.client.model.LeadershipExperienceTypeDto;

public record LeadershipExperienceContextModel(LocalDate currentDate,
        List<LeadershipExperienceTypeDto.LeadershipExperienceKindEnum> kinds) {
}
