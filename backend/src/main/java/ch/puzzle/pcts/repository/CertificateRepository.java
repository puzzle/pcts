package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    @Query("SELECT c from Certificate c where c.member = :member AND c.deletedAt IS NULL")
    List<Certificate> findAllByMember(Member member);
}
