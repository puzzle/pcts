package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberValidationServiceTest extends ValidationBaseServiceTest<Member, MemberValidationService> {

    @InjectMocks
    MemberValidationService service;

    @Override
    Member getModel() {
        return createMember(EmploymentState.MEMBER, Date.valueOf(LocalDate.of(1990, 1, 1)), "Member", "Test", "MT");
    }

    @Override
    MemberValidationService getService() {
        return service;
    }

    private static Member createMember(EmploymentState employmentState, java.util.Date birthDate, String name,
                                       String lastName, String abbreviation) {
        Member m = new Member();
        m.setEmploymentState(employmentState);
        m.setBirthDate(birthDate);
        m.setName(name);
        m.setLastName(lastName);
        m.setAbbreviation(abbreviation);
        m.setDateOfHire(new Timestamp(0L));
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    static Stream<Arguments> invalidModelProvider() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date validPastDate = Date.valueOf(LocalDate.of(1990, 1, 1));

        return Stream
                .of(Arguments
                        .of(createMember(null, validPastDate, "Member", "Test", "MT"),
                            "Member.employmentState must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, null, "Member", "Test", "MT"),
                                "Member.birthDate must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                "Member.birthDate must be in the past, given " + futureDate + "."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, null, "Test", "MT"),
                                "Member.name must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", null, "MT"),
                                "Member.lastName must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", null),
                                "Member.abbreviation must not be null."));
    }
}
