package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitPersistenceService {
    private final OrganisationUnitRepository repository;

    public OrganisationUnitPersistenceService(OrganisationUnitRepository repository) {
        this.repository = repository;
    }

    public OrganisationUnit create(OrganisationUnit organisationUnit) {
        return repository.save(organisationUnit);
    }

    public Optional<OrganisationUnit> getById(Long id) {
        return repository.findById(id);
    }

    public List<OrganisationUnit> getAll() {
        return repository.findAll();
    }

    public Optional<OrganisationUnit> getByName(String name) {
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
