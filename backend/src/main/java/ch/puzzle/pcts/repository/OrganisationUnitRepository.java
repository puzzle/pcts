package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long> {
}
