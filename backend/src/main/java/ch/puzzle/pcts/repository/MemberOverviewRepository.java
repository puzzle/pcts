package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberOverviewRepository extends JpaRepository<MemberOverview, Long> {
    List<MemberOverview> findAllByMemberId(Long id);
}
