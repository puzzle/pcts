package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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

    Member memberWithPtimeId = Member.Builder
            .builder()
            .withFirstName("Member 1")
            .withLastName("Test")
            .withEmploymentState(EmploymentState.EX_MEMBER)
            .withBirthDate(LocalDate.ofYearDay(1999, 1))
            .withPtimeId(1L)
            .build();

    @DisplayName("Should get all entities")
    @Test
    @Transactional
    @Override
    void shouldGetAllEntities() {
        List<Member> all = persistenceService.getAll();
        Assertions.assertThat(all).hasSize(getAll().size());
    }

    @DisplayName("Should return member by ptimeId when found")
    @Test
    void shouldReturnMemberByPtimeID() {
        persistenceService.save(memberWithPtimeId);
        Optional<Member> result = persistenceService.findByPtimeId(MEMBER_1.getPtimeId());

        assertThat(result).isPresent();
        assertThat(result.get().getPtimeId()).isEqualTo(1L);
    }

    @DisplayName("Should not return member by ptimeId when not found")
    @Test
    void shouldNotReturnMemberByPtimeID() {
        Optional<Member> result = persistenceService.findByPtimeId(999L);

        assertThat(result.isEmpty());
    }

    @DisplayName("Should return member by abbreviation when found")
    @Test
    void shouldReturnMemberByAbbreviation() {
        Optional<Member> result = persistenceService.findByAbbreviation("M1");

        assertThat(result).isPresent();
        assertThat(result.get().getAbbreviation()).isEqualTo(MEMBER_1.getAbbreviation());
    }

    @DisplayName("Should not return member by abbreviation when not found")
    @Test
    void shouldNotReturnMemberByAbbreviation() {
        Optional<Member> result = persistenceService.findByAbbreviation("Not an Abbreviation");

        assertThat(result.isEmpty());
    }
}
