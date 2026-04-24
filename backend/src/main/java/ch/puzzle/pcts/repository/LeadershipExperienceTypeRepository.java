package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceTypeRepository extends SoftDeleteRepository<LeadershipExperienceType, Long> {
    Optional<LeadershipExperienceType> findByNameAndDeletedAtIsNull(String name);
}
