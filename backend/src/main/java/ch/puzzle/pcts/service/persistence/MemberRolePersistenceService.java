package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.MEMBER_ROLE;

import ch.puzzle.pcts.model.memberrole.MemberRole;
import ch.puzzle.pcts.repository.MemberRoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberRolePersistenceService extends PersistenceBase<MemberRole, MemberRoleRepository> {
    private final MemberRoleRepository memberRoleRepository;
    public MemberRolePersistenceService(MemberRoleRepository memberRoleRepository) {
        super(memberRoleRepository);
        this.memberRoleRepository = memberRoleRepository;
    }

    public Optional<List<MemberRole>> findByMemberId(Long memberid) {
        return this.memberRoleRepository.findByMemberId(memberid);
    }

    @Override
    public String entityName() {
        return MEMBER_ROLE;
    }
}
