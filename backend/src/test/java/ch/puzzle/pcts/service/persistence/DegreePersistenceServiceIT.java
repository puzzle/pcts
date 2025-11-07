package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.DegreeRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DegreePersistenceServiceIT extends PersistenceBaseIT<Degree, DegreeRepository, DegreePersistenceService> {

    @Autowired
    DegreePersistenceServiceIT(DegreePersistenceService service) {
        super(service);
    }

    @Override
    Degree getModel() {
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        organisationUnit.setDeletedAt(new Timestamp(0));

        DegreeType degreeType = new DegreeType(1L,
                                               "Degree type 1",
                                               new BigDecimal("120.55"),
                                               new BigDecimal("60"),
                                               new BigDecimal("15.45"));

        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withFirstName("Member 1")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation("M1")
                .withDateOfHire(Timestamp.valueOf("2021-07-15 00:00:00"))
                .withBirthDate(Timestamp.valueOf("1999-08-10 00:00:00"))
                .withOrganisationUnit(organisationUnit)
                .build();

        return Degree.Builder
                .builder()
                .withId(null)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution")
                .withCompleted(true)
                .withType(degreeType)
                .withStartDate(Timestamp.valueOf("2015-09-01 00:00:00"))
                .withEndDate(Timestamp.valueOf("2020-06-01 00:00:00"))
                .withComment("Comment")
                .build();
    }

    @Override
    List<Degree> getAll() {
        OrganisationUnit deletedOrganisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        deletedOrganisationUnit.setDeletedAt(new Timestamp(0L));

        OrganisationUnit organisationUnit2 = new OrganisationUnit(2L, "OrganisationUnit 2");

        DegreeType degreeType1 = new DegreeType(1L,
                                                "Degree type 1",
                                                new BigDecimal("120.55"),
                                                new BigDecimal("60"),
                                                new BigDecimal("15.45"));

        DegreeType degreeType2 = new DegreeType(2L,
                                                "Degree type 2",
                                                new BigDecimal("12"),
                                                new BigDecimal("3.961"),
                                                new BigDecimal("3"));

        Member member1 = Member.Builder
                .builder()
                .withId(1L)
                .withFirstName("Member 1")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation("M1")
                .withDateOfHire(Timestamp.valueOf("2021-07-15 00:00:00"))
                .withBirthDate(Timestamp.valueOf("1999-08-10 00:00:00"))
                .withOrganisationUnit(deletedOrganisationUnit)
                .build();

        Member member2 = Member.Builder
                .builder()
                .withId(2L)
                .withFirstName("Member 2")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation("M2")
                .withDateOfHire(Timestamp.valueOf("2020-06-01 00:00:00"))
                .withBirthDate(Timestamp.valueOf("1998-03-03 00:00:00"))
                .withOrganisationUnit(organisationUnit2)
                .build();

        return List
                .of(Degree.Builder
                        .builder()
                        .withId(1L)
                        .withMember(member1)
                        .withName("Degree 1")
                        .withInstitution("Institution")
                        .withCompleted(true)
                        .withType(degreeType1)
                        .withStartDate(Timestamp.valueOf("2015-09-01 00:00:00"))
                        .withEndDate(Timestamp.valueOf("2020-06-01 00:00:00"))
                        .withComment("Comment")
                        .build(),

                    Degree.Builder
                            .builder()
                            .withId(2L)
                            .withMember(member2)
                            .withName("Degree 2")
                            .withInstitution("Institution")
                            .withCompleted(false)
                            .withType(degreeType2)
                            .withStartDate(Timestamp.valueOf("2016-09-01 00:00:00"))
                            .withEndDate(Timestamp.valueOf("2019-06-30 00:00:00"))
                            .withComment("Comment")
                            .build());
    }
}
