package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.MEMBERS;
import static ch.puzzle.pcts.util.TestDataModels.ORG_UNIT_2;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService service) {
        super(service);
    }

    @Override
    Member getModel() {
        return Member.Builder
                .builder()
                .withId(null)
                .withFirstName("Member 3")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(LocalDate.of(2019, 8, 4))
                .withBirthDate(LocalDate.of(1970, 1, 1))
                .withOrganisationUnit(ORG_UNIT_2)
                .build();
    }

    @Override
    List<Member> getAll() {
        return MEMBERS;
    }
}
