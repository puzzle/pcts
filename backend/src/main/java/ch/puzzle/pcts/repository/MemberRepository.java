package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends SoftDeleteRepository<Member, Long> {

}
