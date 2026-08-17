package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.memberrole.MemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository extends SoftDeleteRepository<MemberRole, Long> {
    Optional<List<MemberRole>> findByMemberId(Long memberid);
}
