package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.role.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.deletedAt IS NULL AND r.id = :id")
    Role getByID(@Param("id") Long id);

    @Query("SELECT r FROM Role r WHERE r.deletedAt IS NULL")
    List<Role> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Role r SET r.name = :name, r.isManagement = :isManagement WHERE r.id = :id")
    void update(@Param("id") Long id, @Param("name") String name, @Param("isManagement") boolean isManagement);
}
