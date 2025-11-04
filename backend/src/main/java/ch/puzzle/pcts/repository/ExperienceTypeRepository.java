package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceTypeRepository extends SoftDeleteRepository<ExperienceType, Long> {
    Optional<ExperienceType> findByName(String name);
}
