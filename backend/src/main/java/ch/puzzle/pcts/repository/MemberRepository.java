package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends SoftDeleteRepository<Member, Long> {
    Optional<Member> findByPtimeIdAndIdNotAndPtimeIdNotNull(Long ptimeId, Long id);

    @Query("SELECT m FROM Member m WHERE m.deletedAt IS NULL AND m.ptimeId = :ptimeId")
    Optional<Member> findByPtimeId(Long ptimeId);

    @Query("SELECT m FROM Member m WHERE m.deletedAt IS NULL AND m.abbreviation = :abbreviation")
    Optional<Member> findByAbbreviation(String abbreviation);

    @Query("SELECT m FROM Member m WHERE m.deletedAt IS NULL AND m.ldapName = :ldapName")
    Optional<Member> findMemberByLdapName(String ldapName);
}
