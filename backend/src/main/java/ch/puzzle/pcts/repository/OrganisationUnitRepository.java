package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationUnitRepository
        extends
            JpaRepository<OrganisationUnit, Long>,
            SoftDeleteRepository<OrganisationUnit> {
    Optional<OrganisationUnit> findByName(String name);

    List<OrganisationUnit> findByDeletedAtIsNull();
}
