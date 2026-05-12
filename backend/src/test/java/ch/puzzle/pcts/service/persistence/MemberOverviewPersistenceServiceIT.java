package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.MEMBER_1_OVERVIEWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class MemberOverviewPersistenceServiceIT extends PersistenceCoreIT {

    private final MemberOverviewPersistenceService service;

    @Autowired
    MemberOverviewPersistenceServiceIT(MemberOverviewPersistenceService service) {
        this.service = service;
    }

    @DisplayName("Should get all member overview with member id 1")
    @Test
    void shouldGetAllMemberOverviewRowsForMember1() {
        List<MemberOverview> memberOverviews = service.getById(MEMBER_1_ID);

        assertThat(memberOverviews).isNotNull().hasSize(MEMBER_1_OVERVIEWS.size()).allSatisfy(row -> {
            assertThat(row.getMemberId()).isEqualTo(MEMBER_1_ID);
            assertThat(row.getFirstName()).isEqualTo("Member 1");
            assertThat(row.getLastName()).isEqualTo("Test");
            assertThat(row.getAbbreviation()).isEqualTo("M1");
            assertThat(row.getOrganisationUnitName()).isEqualTo("OrganisationUnit 1");
        });

        assertThat(memberOverviews)
                .extracting(MemberOverview::getCertificateId)
                .containsExactlyInAnyOrder(1L, 1L, 4L, 4L);

        assertThat(memberOverviews).extracting(MemberOverview::getLeadershipExperienceId).containsOnly(1L);

        assertThat(memberOverviews).extracting(MemberOverview::getDegreeId).containsOnly(1L);

        assertThat(memberOverviews)
                .extracting(MemberOverview::getExperienceId)
                .containsExactlyInAnyOrder(1L, 1L, 2L, 2L);

        assertThat(memberOverviews)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(MEMBER_1_OVERVIEWS);
    }

    @DisplayName("Should throw exception when id is not found")
    @Test
    void shouldThrowExceptionWhenIdIsNotFound() {
        Map<FieldKey, String> expectedAttributes = Map
                .of(FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(INVALID_ID),
                    FieldKey.ENTITY,
                    service.entityName());

        PCTSException exception = assertThrows(PCTSException.class, () -> service.getById(INVALID_ID));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(expectedAttributes), exception.getErrorAttributes());
    }

    @DisplayName("Should not retrieve deleted members")
    @Test
    void shouldNotRetrieveMemberWhenIsDeleted() {
        Map<FieldKey, String> expectedAttributes = Map
                .of(FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(DELETED_MEMBER_4_ID),
                    FieldKey.ENTITY,
                    service.entityName());

        PCTSException exception = assertThrows(PCTSException.class, () -> service.getById(DELETED_MEMBER_4_ID));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(expectedAttributes), exception.getErrorAttributes());
    }

    static Stream<Arguments> deletedRelationsProvider() {
        return Stream
                .of(Arguments
                        .of(MEMBER_5_ID,
                            "Certificate",
                            (Function<MemberOverview, Long>) MemberOverview::getCertificateId),
                    Arguments.of(MEMBER_6_ID, "Degree", (Function<MemberOverview, Long>) MemberOverview::getDegreeId),
                    Arguments
                            .of(MEMBER_7_ID,
                                "Experience",
                                (Function<MemberOverview, Long>) MemberOverview::getExperienceId),
                    Arguments
                            .of(MEMBER_8_ID,
                                "Leadership Experience",
                                (Function<MemberOverview, Long>) MemberOverview::getLeadershipExperienceId));
    }

    @DisplayName("Should evaluate soft-deleted relations as 0")
    @ParameterizedTest(name = "Member {0} should have no {1} (id = 0)")
    @MethodSource("deletedRelationsProvider")
    void shouldNotRetrieveDeletedRelations(Long memberId, String relationName,
                                           Function<MemberOverview, Long> idExtractor) {
        List<MemberOverview> memberOverviews = service.getById(memberId);

        assertThat(memberOverviews).isNotEmpty();

        assertThat(memberOverviews)
                .as("The deleted %s should result in a 0L due to COALESCE in the view", relationName)
                .extracting(idExtractor)
                .containsOnly(0L);
    }
}
