package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {
    private final MemberPersistenceService persistenceService;

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService persistenceService) {
        super(persistenceService);
        this.persistenceService = persistenceService;
    }

    @Override
    Member getModel() {
        return MEMBER_2;
    }

    @Override
    List<Member> getAll() {
        return MEMBERS;
    }

    @BeforeEach
    void enrichMembersWithSyncData() {
        MEMBERS.forEach(expectedMember -> {
            Optional<Member> dbMemberOpt = persistenceService.findByAbbreviation(expectedMember.getAbbreviation());

            dbMemberOpt.ifPresent(dbMember -> {
                dbMember.setPtimeId(expectedMember.getPtimeId());
                dbMember.setLastSuccessfulSync(expectedMember.getLastSuccessfulSync());
                dbMember.setSyncErrorCount(expectedMember.getSyncErrorCount());

                persistenceService.save(dbMember);
            });
        });
    }

    @DisplayName("Should return member by ptimeId when found")
    @Test
    void shouldReturnMemberByPtimeID() {
        Optional<Member> result = persistenceService.findByPtimeId(MEMBER_1.getPtimeId());

        assertThat(result).isPresent();
        assertThat(result.get().getPtimeId()).isEqualTo(1L);
    }

    @DisplayName("Should not return member by ptimeId when not found")
    @Test
    void shouldNotReturnMemberByPtimeID() {
        Optional<Member> result = persistenceService.findByPtimeId(999L);

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should return member by LDAP name when found")
    @Test
    void shouldReturnMemberByLdapName() {
        Optional<Member> result = persistenceService.findByLdapName("mtest1");

        assertThat(result).isPresent();
        assertThat(result.get().getLdapName()).isEqualTo(MEMBER_1.getLdapName());
    }

    @DisplayName("Should throw exception when LDAP name does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenLdapNameDoesNotExist() {
        Optional<Member> result = persistenceService.findByLdapName("non-existent");

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should not return member by id when member is deleted")
    @Test
    void shouldNotReturnMemberByIdWhenIsDeleted() {
        Optional<Member> result = persistenceService.findById(DELETED_MEMBER_4.getId());

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should not return member by LDAP name when member is deleted")
    @Test
    void shouldNotReturnMemberByLdapNameWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByLdapName(DELETED_MEMBER_4.getLdapName());

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should not return member by ptimeId when member is deleted")
    @Test
    void shouldNotReturnMemberByPtimeIdWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByPtimeId(DELETED_MEMBER_4.getPtimeId());

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should not return member by abbreviation when member is deleted")
    @Test
    void shouldNotReturnMemberByAbbreviationWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByAbbreviation(DELETED_MEMBER_4.getAbbreviation());

        assertTrue(result.isEmpty());
    }
}
