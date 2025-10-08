package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisationUnitPersistenceService extends PersistenceBase<OrganisationUnit, OrganisationUnitRepository> {
    private final OrganisationUnitRepository repository;

    @Autowired
    public OrganisationUnitPersistenceService(OrganisationUnitRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<OrganisationUnit> getByName(String name) {
        return repository.findByName(name);
    }
}
