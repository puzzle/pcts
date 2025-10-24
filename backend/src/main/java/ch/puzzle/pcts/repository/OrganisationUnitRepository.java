package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long> {
    Optional<OrganisationUnit> findByName(String name);

    @Query(value = "SELECT * FROM organisation_unit WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<OrganisationUnit> findById(Long id);

    @Query(value = "SELECT * FROM organisation_unit WHERE deleted_at IS NULL", nativeQuery = true)
    List<OrganisationUnit> findAll();
}
