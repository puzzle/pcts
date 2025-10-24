package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "SELECT * FROM member WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<Member> findById(Long id);

    @Query(value = "SELECT * FROM member WHERE deleted_at IS NULL", nativeQuery = true)
    List<Member> findAll();
}
