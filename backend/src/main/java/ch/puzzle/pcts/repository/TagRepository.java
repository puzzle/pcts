package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Tag;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM tag t WHERE t.id NOT IN (SELECT tag_id FROM certificate_tag)", nativeQuery = true)
    Set<Tag> findAllUnusedTags();

}
