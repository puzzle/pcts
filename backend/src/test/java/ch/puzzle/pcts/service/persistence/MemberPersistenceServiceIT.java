package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
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
