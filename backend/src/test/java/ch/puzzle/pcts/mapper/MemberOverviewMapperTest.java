package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.MEMBER_1_OVERVIEWS;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.dto.memberoverview.MemberCvDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewMemberDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberOverviewMapperTest {

    @InjectMocks
    private MemberOverviewMapper mapper;

    @DisplayName("Should map List<MemberOverview> to MemberOverviewDto")
    @Test
    void shouldReturnMemberOverviewDto() {
        MemberOverviewDto dto = mapper.toDto(MEMBER_1_OVERVIEWS);

        assertThat(dto).isNotNull();
        assertThat(dto.member()).isNotNull();
        assertThat(dto.cv()).isNotNull();

        MemberOverviewMemberDto member = dto.member();
        assertThat(member.id()).isEqualTo(1L);
        assertThat(member.firstName()).isEqualTo("Member 1");
        assertThat(member.lastName()).isEqualTo("Test");
        assertThat(member.abbreviation()).isEqualTo("M1");
        assertThat(member.employmentState()).isEqualTo(EmploymentState.MEMBER);
        assertThat(member.dateOfHire()).isEqualTo(LocalDate.of(2021, 7, 15));
        assertThat(member.birthDate()).isEqualTo(LocalDate.of(1999, 8, 10));
        assertThat(member.organisationUnitName()).isEqualTo("OrganisationUnit 1");

        MemberCvDto cv = dto.cv();

        assertThat(cv.degrees()).hasSize(1);
        MemberOverviewDegreeDto degree = cv.degrees().getFirst();
        assertThat(degree.id()).isEqualTo(1L);
        assertThat(degree.name()).isEqualTo("Degree 1");
        assertThat(degree.type().name()).isEqualTo("Degree type 1");
        assertThat(degree.startDate()).isEqualTo(LocalDate.of(2015, 9, 1));
        assertThat(degree.endDate()).isEqualTo(LocalDate.of(2020, 6, 1));
        assertThat(degree.comment()).isEqualTo("Comment");

        assertThat(cv.experiences()).hasSize(2);
        assertThat(cv.experiences()).extracting(MemberOverviewExperienceDto::id).containsExactlyInAnyOrder(1L, 2L);

        assertThat(cv.certificates()).hasSize(2);
        assertThat(cv.certificates()).extracting(MemberOverviewCertificateDto::id).containsExactlyInAnyOrder(1L, 4L);

        assertThat(cv.leadershipExperiences()).isEmpty();
    }
}
