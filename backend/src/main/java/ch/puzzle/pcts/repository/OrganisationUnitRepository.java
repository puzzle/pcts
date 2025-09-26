package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long> {
    Optional<OrganisationUnit> findByName(String name);
}
