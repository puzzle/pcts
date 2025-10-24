package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationUnitRepository extends SoftDeleteRepository<OrganisationUnit, Long> {
    Optional<OrganisationUnit> findByName(String name);

}
