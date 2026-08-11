package ch.puzzle.pctsmigration.api;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MembersApi membersApi;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("It should retrieve the member list from the MembersAPI and return it")
    void getMembers_returnsListFromApi() throws Exception {
        MemberDto member1 = createMember(1L, "AW");
        MemberDto member2 = createMember(2L, "JN");
        List<MemberDto> expectedMembers = List.of(member1, member2);

        when(membersApi.getMember()).thenReturn(expectedMembers);

        List<MemberDto> actualMembers = memberService.getMembers();

        assertThat(actualMembers)
                .isNotNull()
                .hasSize(2)
                .containsExactlyElementsOf(expectedMembers);

        verify(membersApi, times(1)).getMember();
    }

    @Test
    @DisplayName("Should ApiException be rethrown if the API call fails?")
    void getMembers_whenApiThrowsException_throwsApiException() throws Exception {
        when(membersApi.getMember()).thenThrow(new ApiException("API Error"));

        assertThatThrownBy(() -> memberService.getMembers())
                .isInstanceOf(ApiException.class)
                .hasMessage("API Error");
    }


    @Test
    @DisplayName("Should return the member's ID if the abbreviation matches")
    void getMemberIdBy_whenAbbreviationMatches_returnsMemberId() throws Exception {
        MemberDto member1 = createMember(10L, "AW");
        MemberDto member2 = createMember(20L, "RR");
        when(membersApi.getMember()).thenReturn(List.of(member1, member2));

        Long resultId = memberService.getMemberIdBy("RR");

        assertThat(resultId).isEqualTo(20L);
        verify(membersApi, times(1)).getMember();
    }

    @Test
    @DisplayName("Should return null if no abbreviation matches")
    void getMemberIdBy_whenAbbreviationNotFound_returnsNull() throws Exception {
        MemberDto member1 = createMember(10L, "AW");
        when(membersApi.getMember()).thenReturn(List.of(member1));

        Long resultId = memberService.getMemberIdBy("UNKNOWN");

        assertThat(resultId).isNull();
    }

    @Test
    @DisplayName("Should return null if null is passed as a shorthand and no member has a value of null")
    void getMemberIdBy_whenSearchingForNull_returnsNull() throws Exception {
        MemberDto member1 = createMember(10L, "AW");
        when(membersApi.getMember()).thenReturn(List.of(member1));

        Long resultId = memberService.getMemberIdBy(null);

        assertThat(resultId).isNull();
    }

    @Test
    @DisplayName("Should ID be found when searching for null and a member has a null abbreviation?")
    void getMemberIdBy_whenMemberHasNullAbbreviationAndSearchingForNull_returnsMemberId() throws Exception {
        MemberDto memberWithNullAbbr = createMember(99L, null);
        when(membersApi.getMember()).thenReturn(List.of(memberWithNullAbbr));

        Long resultId = memberService.getMemberIdBy(null);

        assertThat(resultId).isEqualTo(99L);
    }

    @Test
    @DisplayName("Should ApiException be rethrown if the internal call to getMembers() fails?")
    void getMemberIdBy_whenApiThrowsException_throwsApiException() throws Exception {
        when(membersApi.getMember()).thenThrow(new ApiException("HTTP 404"));

        assertThatThrownBy(() -> memberService.getMemberIdBy("AW"))
                .isInstanceOf(ApiException.class)
                .hasMessage("HTTP 404");
    }

    private MemberDto createMember(Long id, String abbreviation) {
        MemberDto dto = new MemberDto(id);
        dto.setAbbreviation(abbreviation);
        return dto;
    }
}