package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.role.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, SoftDeleteRepository<Role> {
    Optional<Role> findByName(String name);
}
