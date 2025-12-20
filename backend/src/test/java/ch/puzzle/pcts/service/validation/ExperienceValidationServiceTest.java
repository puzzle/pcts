package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.EXPERIENCE;
import static ch.puzzle.pcts.service.validation.MemberValidationServiceTest.createMember;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        ExperienceType experienceType = ExperienceType.Builder
                .builder()
                .withName("Experience Type")
                .withHighlyRelevantPoints(BigDecimal.valueOf(7.5))
                .withLimitedRelevantPoints(BigDecimal.valueOf(4.75))
                .withLittleRelevantPoints(BigDecimal.valueOf(3))
                .build();

        Member member = createMember(EmploymentState.MEMBER, LocalDate.EPOCH, "Member", "Test", "MT", "test@puzzle.ch");

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
        int validPercentage = 100;

        return Stream
                .of(Arguments
                        .of(createExperience(null, DATE_NOW, DATE_TOMORROW, validPercentage),
                            List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createExperience("", DATE_NOW, DATE_TOMORROW, validPercentage),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createExperience("  ", DATE_NOW, DATE_TOMORROW, validPercentage),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createExperience("S", DATE_NOW, DATE_TOMORROW, validPercentage),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Experience",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createExperience("  S ", DATE_NOW, DATE_TOMORROW, validPercentage),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Experience",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createExperience(TOO_LONG_STRING, DATE_NOW, DATE_TOMORROW, validPercentage),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Experience",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createExperience("Experience", DATE_TOMORROW, DATE_TOMORROW, validPercentage),
                                List.of(Map.of(FieldKey.IS, "{attribute.date.past.present}"))),
                    Arguments
                            .of(createExperience("Experience", null, DATE_NOW, validPercentage),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "startDate"))),
                    Arguments
                            .of(new Experience.Builder()
                                    .withMember(createMember(EmploymentState.MEMBER,
                                                             LocalDate.EPOCH,
                                                             "Member",
                                                             "Test",
                                                             "MT",
                                                             "test@puzzle.ch"))
                                    .withName("Experience")
                                    .withEmployer("Employer")
                                    .withPercent(100)
                                    .withComment("Comment test")
                                    .withStartDate(DATE_NOW)
                                    .withEndDate(DATE_NOW)
                                    .build(),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "type"))),
                    Arguments
                            .of(createExperience("Experience", DATE_YESTERDAY, DATE_NOW, -1),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "percent"))),
                    Arguments
                            .of(createExperience("Experience", DATE_YESTERDAY, DATE_NOW, 115),
                                List.of(Map.of(FieldKey.CLASS, "Experience", FieldKey.FIELD, "percent"))));
    }

    @Override
    Experience getValidModel() {
        return createExperience("Experience", LocalDate.of(2021, 7, 15), LocalDate.of(2022, 7, 15), 100);
    }

    @Override
    ExperienceValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on ValidateOnUpdate and ValidateOnCreate when endDate is before startDate")
    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        int validPercentage = 50;

        Experience experience = createExperience("Experience", DATE_NOW, DATE_YESTERDAY, validPercentage);

        List<PCTSException> exceptions = List
                .of(assertThrows(PCTSException.class, () -> service.validateOnUpdate(EXPERIENCE_1_ID, experience)),
                    assertThrows(PCTSException.class, () -> service.validateOnCreate(experience)));

        exceptions
                .stream()
                .forEach(exception -> assertEquals(List
                        .of(Map
                                .of(FieldKey.ENTITY,
                                    EXPERIENCE,
                                    FieldKey.FIELD,
                                    "startDate",
                                    FieldKey.IS,
                                    DATE_NOW.toString(),
                                    FieldKey.CONDITION_FIELD,
                                    "endDate",
                                    FieldKey.MAX,
                                    DATE_YESTERDAY.toString())), exception.getErrorAttributes()));
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
        Experience experience = getValidModel();

        doNothing().when((ValidationBase<Experience>) service).validateOnUpdate(anyLong(), any());

        service.validateOnUpdate(EXPERIENCE_1_ID, experience);

        verify(service).validateOnUpdate(EXPERIENCE_1_ID, experience);
        verifyNoMoreInteractions(persistenceService);
    }
}
