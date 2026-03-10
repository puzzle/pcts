package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceKind;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceTypeValidationServiceTest
        extends
            ValidationBaseServiceTest<LeadershipExperienceType, LeadershipExperienceTypeValidationService> {

    @InjectMocks
    LeadershipExperienceTypeValidationService service;

    @Mock
    private LeadershipExperienceTypePersistenceService persistenceService;

    @Override
    LeadershipExperienceType getValidModel() {
        return LeadershipExperienceType.Builder
                .builder()
                .withName("Leadership Experience Type")
                .withPoints(BigDecimal.valueOf(10))
                .withComment("Comment")
                .withExperienceKind(LeadershipExperienceKind.LEADERSHIP_TRAINING)
                .build();
    }

    @Override
    LeadershipExperienceTypeValidationService getService() {
        return service;
    }

    private static LeadershipExperienceType createLeadershipExperienceType(String name, BigDecimal points,
                                                                           LeadershipExperienceKind experienceKind) {
        return LeadershipExperienceType.Builder
                .builder()
                .withName(name)
                .withPoints(points)
                .withComment("Comment")
                .withExperienceKind(experienceKind)
                .build();
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createLeadershipExperienceType(null,
                                                           POSITIVE_BIG_DECIMAL,
                                                           LeadershipExperienceKind.LEADERSHIP_TRAINING),
                            List.of(Map.of(FieldKey.CLASS, "LeadershipExperienceType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createLeadershipExperienceType("",
                                                               POSITIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List.of(Map.of(FieldKey.CLASS, "LeadershipExperienceType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createLeadershipExperienceType("  ",
                                                               POSITIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List.of(Map.of(FieldKey.CLASS, "LeadershipExperienceType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createLeadershipExperienceType("S",
                                                               POSITIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),

                    Arguments
                            .of(createLeadershipExperienceType("  S ",
                                                               POSITIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),

                    Arguments
                            .of(createLeadershipExperienceType(TOO_LONG_STRING,
                                                               POSITIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),

                    Arguments
                            .of(createLeadershipExperienceType("LeadershipExperience",
                                                               null,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List.of(Map.of(FieldKey.CLASS, "LeadershipExperienceType", FieldKey.FIELD, "points"))),

                    Arguments
                            .of(createLeadershipExperienceType("LeadershipExperience",
                                                               NEGATIVE_BIG_DECIMAL,
                                                               LeadershipExperienceKind.LEADERSHIP_TRAINING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceType",
                                                    FieldKey.FIELD,
                                                    "points",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createLeadershipExperienceType("LeadershipExperience", POSITIVE_BIG_DECIMAL, null),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceType",
                                                    FieldKey.FIELD,
                                                    "experienceKind"))));
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        LeadershipExperienceType leadershipExperienceType = getValidModel();

        when(persistenceService.getByName(leadershipExperienceType.getName()))
                .thenReturn(Optional.of(new LeadershipExperienceType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperienceType));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "name",
                            FieldKey.IS,
                            "Leadership Experience Type",
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        LeadershipExperienceType newLeadershipExperience = getValidModel();
        LeadershipExperienceType leadershipExperience = getValidModel();
        leadershipExperience.setId(LEADERSHIP_TYPE_2_ID);

        when(persistenceService.getByName(newLeadershipExperience.getName()))
                .thenReturn(Optional.of(leadershipExperience));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateOnUpdate(LEADERSHIP_TYPE_1_ID,
                                                                         newLeadershipExperience));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "name",
                            FieldKey.IS,
                            "Leadership Experience Type",
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        LeadershipExperienceType leadershipExperienceType = getValidModel();

        LeadershipExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<LeadershipExperienceType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(leadershipExperienceType);

        verify(spyService).validateOnCreate(leadershipExperienceType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        LeadershipExperienceType leadershipExperience = getValidModel();

        LeadershipExperienceTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<LeadershipExperienceType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(LEADERSHIP_TYPE_1_ID, leadershipExperience);

        verify(spyService).validateOnUpdate(LEADERSHIP_TYPE_1_ID, leadershipExperience);
        verifyNoMoreInteractions(persistenceService);
    }
}
