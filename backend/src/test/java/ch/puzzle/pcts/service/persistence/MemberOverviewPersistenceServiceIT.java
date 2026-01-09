package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.MEMBER_1_OVERVIEWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        List<MemberOverview> memberOverviews = service.getById(1L);

        assertThat(memberOverviews).isNotNull();
        assertThat(memberOverviews).hasSize(4);

        assertThat(memberOverviews).allSatisfy(row -> {
            assertThat(row.getMemberId()).isEqualTo(1L);
            assertThat(row.getFirstName()).isEqualTo("Member 1");
            assertThat(row.getLastName()).isEqualTo("Test");
            assertThat(row.getAbbreviation()).isEqualTo("M1");
            assertThat(row.getOrganisationUnitName()).isEqualTo("OrganisationUnit 1");
        });

        assertThat(memberOverviews)
                .extracting(MemberOverview::getCertificateId)
                .containsExactlyInAnyOrder(1L, 1L, 4L, 4L);

        assertThat(memberOverviews).extracting(MemberOverview::getDegreeId).containsOnly(1L);

        assertThat(memberOverviews)
                .extracting(MemberOverview::getExperienceId)
                .containsExactlyInAnyOrder(1L, 2L, 1L, 2L);

        assertThat(memberOverviews)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(MEMBER_1_OVERVIEWS);
    }

    @DisplayName("Should throw exception when id is not found")
    @Test
    void shouldThrowExceptionWhenIdIsNotFound() {
        Long invalidId = -1L;

        Map<FieldKey, String> expectedAttributes = Map
                .of(FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(invalidId),
                    FieldKey.ENTITY,
                    service.entityName());

        PCTSException exception = assertThrows(PCTSException.class, () -> service.getById(invalidId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(expectedAttributes), exception.getErrorAttributes());
    }
}
