package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisationUnitPersistenceService {
    private final OrganisationUnitRepository repository;

    @Autowired
    public OrganisationUnitPersistenceService(OrganisationUnitRepository repository) {
        this.repository = repository;
    }

    public OrganisationUnit create(OrganisationUnit organisationUnit) {
        return repository.save(organisationUnit);
    }

    public Optional<OrganisationUnit> getById(long id) {
        return repository.findById(id);
    }

    public List<OrganisationUnit> getAll() {
        return repository.findAll();
    }

    public OrganisationUnit getByName(String name) {
        return repository.findByName(name);
    }

    public OrganisationUnit update(Long id, OrganisationUnit organisationUnit) {
        organisationUnit.setId(id);
        return repository.save(organisationUnit);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
