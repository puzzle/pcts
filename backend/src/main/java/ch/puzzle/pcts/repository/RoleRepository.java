package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query(value = "SELECT * FROM role WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<Role> findById(Long id);

    @Query(value = "SELECT * FROM role WHERE deleted_at IS NULL", nativeQuery = true)
    List<Role> findAll();
}
