package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.util.TestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberOverviewPersistenceServiceIT extends PersistenceCoreIT {

    protected final MemberOverviewPersistenceService service;

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
                .containsExactlyElementsOf(TestData.MEMBER_1_OVERVIEWS);
    }
}
