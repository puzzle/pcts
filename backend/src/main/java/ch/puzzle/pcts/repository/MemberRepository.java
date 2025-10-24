package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, SoftDeleteRepository<Member> {

}
