package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.MemberRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
                .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                .build();
    }

    @Override
    List<Member> getAll() {
        OrganisationUnit deletedOrganisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        deletedOrganisationUnit.setDeletedAt(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));

        return List
                .of(Member.Builder
                        .builder()
                        .withId(1L)
                        .withFirstName("Member 1")
                        .withLastName("Test")
                        .withEmploymentState(EmploymentState.MEMBER)
                        .withAbbreviation("M1")
                        .withDateOfHire(LocalDate.of(2021, 7, 15))
                        .withBirthDate(LocalDate.of(1999, 8, 10))
                        .withOrganisationUnit(deletedOrganisationUnit)
                        .build(),
                    Member.Builder
                            .builder()
                            .withId(2L)
                            .withFirstName("Member 2")
                            .withLastName("Test")
                            .withEmploymentState(EmploymentState.MEMBER)
                            .withAbbreviation("M2")
                            .withDateOfHire(LocalDate.of(2020, 6, 1))
                            .withBirthDate(LocalDate.of(1998, 3, 3))
                            .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                            .build());
    }
}
