package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceTypeRepository extends JpaRepository<ExperienceType, Long> {
    Optional<ExperienceType> findByName(String name);
}
