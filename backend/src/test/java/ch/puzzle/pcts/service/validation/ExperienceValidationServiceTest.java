package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.service.validation.MemberValidationServiceTest.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Mock
    private ExperiencePersistenceService persistenceService;

    @Spy
    @InjectMocks
    ExperienceValidationService service;

    protected static Experience createExperience(String name, LocalDate startDate, LocalDate endDate) {
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
                .withPercent(100)
                .withType(experienceType)
                .withComment("Comment test")
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

    @Override
    Experience getValidModel() {
        return createExperience("Experience", LocalDate.of(2021, 7, 15), LocalDate.of(2022, 7, 15));
    }

    @Override
    ExperienceValidationService getService() {
        return service;
    }

    static Stream<Arguments> invalidModelProvider() {
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        LocalDate futureDate = today.plusDays(1);
        String tooLongString = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments.of(createExperience(null, today, futureDate), "Experience.name must not be null."),
                    Arguments.of(createExperience("", today, futureDate), "Experience.name must not be blank."),
                    Arguments.of(createExperience("  ", today, futureDate), "Experience.name must not be blank."),
                    Arguments
                            .of(createExperience("S", today, futureDate),
                                "Experience.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createExperience("  S ", today, futureDate),
                                "Experience.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createExperience(tooLongString, today, futureDate),
                                String
                                        .format("Experience.name size must be between 2 and 250, given %s.",
                                                tooLongString)),

                    Arguments
                            .of(createExperience("Experience", futureDate, futureDate),
                                "Experience.startDate must be in the past or present, given " + futureDate + ".",

                                Arguments
                                        .of(createExperience("Experience", null, today),
                                            "Experience.startDate must not be null."),
                                Arguments
                                        .of(createExperience("Experience", today, null),
                                            "Experience.endDate must not be null."),
                                Arguments
                                        .of(createExperience("Experience", today, pastDate),
                                            String
                                                    .format("Experience.endDate must be after or equal to startDate, given %s and %s.",
                                                            today.minusDays(1),
                                                            today)),

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
                                            "Experience.type must not be null.")));
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
