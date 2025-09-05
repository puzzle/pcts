package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceService {
    private final RoleRepository repository;

    @Autowired
    public RolePersistenceService(RoleRepository repository) {
        this.repository = repository;
    }
}
