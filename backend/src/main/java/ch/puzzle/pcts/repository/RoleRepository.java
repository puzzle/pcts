package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.role.Role;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
