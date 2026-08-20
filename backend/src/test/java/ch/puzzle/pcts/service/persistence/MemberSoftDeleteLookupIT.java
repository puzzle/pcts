package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.DELETED_MEMBER_4_ID;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.member.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberSoftDeleteLookupIT extends PersistenceCoreIT {

    private final MemberPersistenceService persistenceService;

    @Autowired
    MemberSoftDeleteLookupIT(MemberPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @DisplayName("Should not return member by id when member is deleted")
    @Test
    void shouldNotReturnMemberByIdWhenIsDeleted() {
        Optional<Member> result = persistenceService.findById(DELETED_MEMBER_4_ID);

        assertThat(result).isEmpty();
    }

    @DisplayName("Should not return member by LDAP name when member is deleted")
    @Test
    void shouldNotReturnMemberByLdapNameWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByLdapName("mtest4");

        assertThat(result).isEmpty();
    }

    @DisplayName("Should not return member by ptimeId when member is deleted")
    @Test
    void shouldNotReturnMemberByPtimeIdWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByPtimeId(4L);

        assertThat(result).isEmpty();
    }

    @DisplayName("Should not return member by abbreviation when member is deleted")
    @Test
    void shouldNotReturnMemberByAbbreviationWhenIsDeleted() {
        Optional<Member> result = persistenceService.findByAbbreviation("M4");

        assertThat(result).isEmpty();
    }
}
