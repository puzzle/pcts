package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.MEMBERS;
import static ch.puzzle.pcts.util.TestData.ORG_UNIT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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

        Optional<Member> result = service.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getFirstName()).isEqualTo("Member 2");
    }

    @DisplayName("Should return empty when email does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Member> result = service.findByEmail("non-existent@puzzle.ch");

        assertThat(result).isNotPresent();
    }

}
