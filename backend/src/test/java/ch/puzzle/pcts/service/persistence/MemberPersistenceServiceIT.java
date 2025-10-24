package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.MemberRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService service) {
        super(service);
    }

    /**
     * Returns an object of the entity used to save it in the database
     */
    @Override
    Member getModel() {
        return Member.Builder
                .builder()
                .withId(null)
                .withName("Member 3")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(new Timestamp(0L))
                .withBirthDate(new Timestamp(0L))
                .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                .build();
    }

    /**
     * A list of all the objects with this datatype stored in the database shouldn't
     * contain soft deleted ones
     */
    @Override
    List<Member> getAll() {
        OrganisationUnit deletedOrganisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        deletedOrganisationUnit.setDeletedAt(Timestamp.from(Instant.ofEpochSecond(0)));
        return List
                .of(Member.Builder
                        .builder()
                        .withId(1L)
                        .withName("Member 1")
                        .withLastName("Test")
                        .withEmploymentState(EmploymentState.MEMBER)
                        .withAbbreviation("M1")
                        .withDateOfHire(Timestamp.valueOf("2021-07-15 00:00:00"))
                        .withBirthDate(Timestamp.valueOf("1999-08-10 00:00:00"))
                        .withOrganisationUnit(deletedOrganisationUnit)
                        .build(),
                    Member.Builder
                            .builder()
                            .withId(2L)
                            .withName("Member 2")
                            .withLastName("Test")
                            .withEmploymentState(EmploymentState.MEMBER)
                            .withAbbreviation("M2")
                            .withDateOfHire(Timestamp.valueOf("2020-06-01 00:00:00"))
                            .withBirthDate(Timestamp.valueOf("1998-03-03 00:00:00"))
                            .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                            .build());
    }
}
