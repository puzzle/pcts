package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.DegreeRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        organisationUnit.setDeletedAt(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));

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
                .withDateOfHire(LocalDate.of(2021, 7, 15))
                .withBirthDate(LocalDate.of(1999, 8, 10))
                .withOrganisationUnit(organisationUnit)
                .build();

        return Degree.Builder
                .builder()
                .withId(null)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution")
                .withCompleted(true)
                .withDegreeType(degreeType)
                .withStartDate(LocalDate.of(2015, 9, 1))
                .withEndDate(LocalDate.of(2020, 6, 1))
                .withComment("Comment")
                .build();
    }

    @Override
    List<Degree> getAll() {
        OrganisationUnit deletedOrganisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        deletedOrganisationUnit.setDeletedAt(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));

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
                .withDateOfHire(LocalDate.of(2021, 7, 15))
                .withBirthDate(LocalDate.of(1999, 8, 10))
                .withOrganisationUnit(deletedOrganisationUnit)
                .build();

        Member member2 = Member.Builder
                .builder()
                .withId(2L)
                .withFirstName("Member 2")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation("M2")
                .withDateOfHire(LocalDate.of(2020, 6, 1))
                .withBirthDate(LocalDate.of(1998, 3, 3))
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
                        .withDegreeType(degreeType1)
                        .withStartDate(LocalDate.of(2015, 9, 1))
                        .withEndDate(LocalDate.of(2020, 6, 1))
                        .withComment("Comment")
                        .build(),

                    Degree.Builder
                            .builder()
                            .withId(2L)
                            .withMember(member2)
                            .withName("Degree 2")
                            .withInstitution("Institution")
                            .withCompleted(false)
                            .withDegreeType(degreeType2)
                            .withStartDate(LocalDate.of(2016, 9, 1))
                            .withEndDate(LocalDate.of(2019, 6, 30))
                            .withComment("Comment")
                            .build());
    }
}
