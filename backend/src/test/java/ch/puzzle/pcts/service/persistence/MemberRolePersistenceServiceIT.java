package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberrole.MemberRole;
import ch.puzzle.pcts.repository.MemberRoleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberRolePersistenceServiceIT
        extends
            PersistenceBaseIT<MemberRole, MemberRoleRepository, MemberRolePersistenceService> {
    private final MemberRolePersistenceService persistenceService;

    @Autowired
    MemberRolePersistenceServiceIT(MemberRolePersistenceService persistenceService) {
        super(persistenceService);
        this.persistenceService = persistenceService;
    }

    @Override
    MemberRole getModel() {
        return MEMBERROLE_3;
    }

    @Override
    List<MemberRole> getAll() {
        return MEMBER_ROLES;
    }

    @DisplayName("Should return member by ptimeId when found")
    @Test
    void shouldReturnMemberByPtimeID() {
        Optional<List<MemberRole>> result = persistenceService.findByMemberId(MEMBER_1.getId());

        assertThat(result.isPresent());
        assertThat(result.get().getFirst().getMemberId()).isEqualTo(MEMBER_1.getId());
    }

    @DisplayName("Should not return member by ptimeId when not found")
    @Test
    void shouldNotReturnMemberByPtimeID() {

        assertThrows(PCTSException.class, () -> persistenceService.getById(999L));
    }

    @DisplayName("Should return member by LDAP name when found")
    @Test
    void shouldReturnMemberByLdapName() {
        MemberRole result = persistenceService.getById(1L);

        assertThat(result != null);
        assertThat(result.getId().equals(1L));
    }

    @DisplayName("Should throw exception when LDAP name does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenLdapNameDoesNotExist() {
        Optional<List<MemberRole>> result = persistenceService.findByMemberId(999L);

        assert (result.get().isEmpty());
    }
}
