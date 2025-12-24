package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.member.Member;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends SoftDeleteRepository<Member, Long> {
    Optional<Member> findMemberByEmailAndEmailIsNotNull(String email);
}
