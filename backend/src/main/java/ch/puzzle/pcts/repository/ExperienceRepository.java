package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experience.Experience;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends SoftDeleteRepository<Experience, Long> {
}
