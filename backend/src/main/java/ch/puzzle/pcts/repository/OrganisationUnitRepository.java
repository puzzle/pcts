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

    @Query("SELECT o FROM OrganisationUnit o WHERE o.deletedAt IS NULL")
    List<OrganisationUnit> findAll();

    @Query("SELECT o FROM OrganisationUnit o WHERE o.deletedAt IS NULL AND o.id = :id")
    Optional<OrganisationUnit> findById(Long id);
}
