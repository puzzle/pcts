package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experienceType.ExperienceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceTypeRepository extends JpaRepository<ExperienceType, Long> {
}
