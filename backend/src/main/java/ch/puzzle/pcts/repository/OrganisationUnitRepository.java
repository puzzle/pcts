package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long> {
    Optional<OrganisationUnit> findByName(String name);
}
