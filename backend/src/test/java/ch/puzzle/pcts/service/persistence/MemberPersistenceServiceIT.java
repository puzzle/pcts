package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService service) {
        super(service);
    }

    @Override
    Member getModel() {
        return MEMBER_2;
    }

    @Override
    List<Member> getAll() {
        return MEMBERS;
    }

    @DisplayName("Should get member by email")
    @Transactional
    @Test
    void shouldGetMemberByEmail() {
        String email = "member2@puzzle.ch";

        Member result = persistenceService.getByEmail(email);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getFirstName()).isEqualTo("Member 2");
    }

    @DisplayName("Should throw exception when email does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExistForGet() {
        assertThrows(PCTSException.class, () -> persistenceService.getByEmail("non-existent@puzzle.ch"));
    }

    @DisplayName("Should find member by email")
    @Transactional
    @Test
    void shouldFindMemberByEmail() {
        String email = "member2@puzzle.ch";

        Optional<Member> result = persistenceService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getFirstName()).isEqualTo("Member 2");
    }

    @DisplayName("Should return empty when email does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExistForFind() {
        Optional<Member> result = persistenceService.findByEmail("non-existent@puzzle.ch");

        assertThat(result).isNotPresent();
    }
}
