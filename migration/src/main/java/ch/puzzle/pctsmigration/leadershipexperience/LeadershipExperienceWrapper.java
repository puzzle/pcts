package ch.puzzle.pctsmigration.leadershipexperience;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public class LeadershipExperienceWrapper extends AiResultWrapper<LeadershipExperienceAiResultDto> {

    public LeadershipExperienceWrapper() {
        super(null);
    }

    public LeadershipExperienceWrapper(List<LeadershipExperienceAiResultDto> itmes) {
        super(itmes);
    }
}
