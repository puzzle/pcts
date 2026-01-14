package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberOverviewMapperTest {

    @InjectMocks
    private MemberOverviewMapper mapper;

    @DisplayName("Should map valid MemberOverview lists to MemberOverviewDto")
    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("provideMemberOverviews")
    void shouldReturnMemberOverviewDto(String description, List<MemberOverview> inputOverviews,
                                       MemberOverviewDto expectedDto) {
        MemberOverviewDto actualDto = mapper.toDto(inputOverviews);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should return null when input list is null")
    void shouldReturnNullWhenInputIsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Should return null when input list is empty")
    void shouldReturnNullWhenInputIsEmpty() {
        assertThat(mapper.toDto(Collections.emptyList())).isNull();
    }

    private static Stream<Arguments> provideMemberOverviews() {
        return Stream
                .of(Arguments.of("Standard Member (Full CV)", MEMBER_1_OVERVIEWS, MEMBER_1_OVERVIEW_DTO),
                    Arguments.of("Member with multiple same-type items", MEMBER_2_OVERVIEWS, MEMBER_2_OVERVIEW_DTO),
                    Arguments.of("Member with NO CV data (Sparse)", MEMBER_EMPTY_CV_OVERVIEWS, MEMBER_EMPTY_CV_DTO));
    }

    @Test
    @DisplayName("Should ignore CV rows when IDs are 0")
    void shouldIgnoreZeroIds() {
        MemberOverview row = new MemberOverview();

        row.setMemberId(1L);
        row.setFirstName("Max");
        row.setLastName("Muster");
        row.setEmploymentState(EmploymentState.EX_MEMBER);
        row.setAbbreviation("MM");
        row.setOrganisationUnitName("IT");

        row.setDegreeId(0L);
        row.setExperienceId(0L);
        row.setCertificateId(0L);

        List<MemberOverview> input = List.of(row);

        MemberOverviewDto dto = mapper.toDto(input);

        assertThat(dto).isNotNull();

        assertThat(dto.member().id()).isEqualTo(1L);
        assertThat(dto.member().firstName()).isEqualTo("Max");
        assertThat(dto.member().lastName()).isEqualTo("Muster");

        assertThat(dto.cv().degrees()).isEmpty();
        assertThat(dto.cv().experiences()).isEmpty();
        assertThat(dto.cv().certificates()).isEmpty();
        assertThat(dto.cv().leadershipExperiences()).isEmpty();
    }
}