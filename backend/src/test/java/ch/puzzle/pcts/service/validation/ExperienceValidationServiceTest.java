package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.service.validation.MemberValidationServiceTest.createMember;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceValidationServiceTest extends ValidationBaseServiceTest<Experience, ExperienceValidationService> {

    @Spy
    @InjectMocks
    ExperienceValidationService service;

    @Mock
    private ExperiencePersistenceService persistenceService;

    protected static Experience createExperience(String name, LocalDate startDate, LocalDate endDate, int percent) {
        ExperienceType experienceType = new ExperienceType(null,
                                                           "Experience Type",
                                                           BigDecimal.valueOf(7.5),
                                                           BigDecimal.valueOf(4.75),
                                                           BigDecimal.valueOf(3));

        Member member = createMember(EmploymentState.MEMBER, LocalDate.EPOCH, "Member", "Test", "MT");

        return new Experience.Builder()
                .withMember(member)
                .withName(name)
                .withEmployer("Employer")
                .withPercent(percent)
                .withType(experienceType)
                .withComment("Comment test")
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

    static Stream<Arguments> invalidModelProvider() {
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        LocalDate futureDate = today.plusDays(1);
        String tooLongString = new String(new char[251]).replace("\0", "s");
        int validPercentage = 100;

        return Stream
                .of(Arguments
                        .of(createExperience(null, today, futureDate, validPercentage),
                            "Experience.name must not be null."),
                    Arguments
                            .of(createExperience("", today, futureDate, validPercentage),
                                "Experience.name must not be blank."),
                    Arguments
                            .of(createExperience("  ", today, futureDate, validPercentage),
                                "Experience.name must not be blank."),
                    Arguments
                            .of(createExperience("S", today, futureDate, validPercentage),
                                "Experience.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createExperience("  S ", today, futureDate, validPercentage),
                                "Experience.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createExperience(tooLongString, today, futureDate, validPercentage),
                                String
                                        .format("Experience.name size must be between 2 and 250, given %s.",
                                                tooLongString)),
                    Arguments
                            .of(createExperience("Experience", futureDate, futureDate, validPercentage),
                                "Experience.startDate must be in the past or present, given " + futureDate + "."),
                    Arguments
                            .of(createExperience("Experience", null, today, validPercentage),
                                "Experience.startDate must not be null."),
                    Arguments
                            .of(new Experience.Builder()
                                    .withMember(createMember(EmploymentState.MEMBER,
                                                             LocalDate.EPOCH,
                                                             "Member",
                                                             "Test",
                                                             "MT"))
                                    .withName("Experience")
                                    .withEmployer("Employer")
                                    .withPercent(100)
                                    .withComment("Comment test")
                                    .withStartDate(today)
                                    .withEndDate(today)
                                    .build(),
                                "Experience.type must not be null."),
                    Arguments
                            .of(createExperience("Experience", pastDate, today, -1),
                                String.format("Experience.percent must be greater than or equal to 0, given %s.", -1)),
                    Arguments
                            .of(createExperience("Experience", pastDate, today, 115),
                                String.format("Experience.percent must be less than or equal to 110, given %s.", 115)));
    }

    @Override
    Experience getValidModel() {
        return createExperience("Experience", LocalDate.of(2021, 7, 15), LocalDate.of(2022, 7, 15), 100);
    }

    @Override
    ExperienceValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception ValidateOnUpdate and ValidateOnCreate when endDate is before startDate")
    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        int validPercentage = 50;

        Experience experience = createExperience("Experience", today, pastDate, validPercentage);

        List<PCTSException> exceptions = List
                .of(assertThrows(PCTSException.class, () -> service.validateOnUpdate(1L, experience)),
                    assertThrows(PCTSException.class, () -> service.validateOnCreate(experience)));

        exceptions
                .stream()
                .forEach(exception -> assertEquals(String
                        .format("Experience.endDate must be after the startDate, given endDate: %s and startDate: %s.",
                                pastDate,
                                today), exception.getReason()));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Experience experience = getValidModel();

        doNothing().when((ValidationBase<Experience>) service).validateOnCreate(any());

        service.validateOnCreate(experience);

        verify(service).validateOnCreate(experience);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Experience experience = getValidModel();

        doNothing().when((ValidationBase<Experience>) service).validateOnUpdate(anyLong(), any());

        service.validateOnUpdate(id, experience);

        verify(service).validateOnUpdate(id, experience);
        verifyNoMoreInteractions(persistenceService);
    }
}
