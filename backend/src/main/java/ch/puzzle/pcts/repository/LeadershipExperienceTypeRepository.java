package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceTypeRepository extends SoftDeleteRepository<LeadershipExperienceType, Long> {
    Optional<LeadershipExperienceType> findByNameOfLeadershipExperienceType(String name);

    List<LeadershipExperienceType> findAllOfLeadershipExperienceType();

    Optional<LeadershipExperienceType> findByIdNotAndDeletedAtIsNull(Long id);

    default Optional<LeadershipExperienceType> findByIdOfLeadershipExperienceType(Long id) {
        return findByIdNotAndDeletedAtIsNull(id);
    }
}
