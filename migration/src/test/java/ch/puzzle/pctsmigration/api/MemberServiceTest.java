package ch.puzzle.pctsmigration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MembersApi membersApi;

    @InjectMocks
    private MemberService memberService;

    private MemberDto member1;
    private MemberDto member2;
    private List<MemberDto> memberList;

    @BeforeEach
    void setUp() {
        member1 = new MemberDto(10L);
        member1.setAbbreviation("PUZ");

        member2 = new MemberDto(20L);
        member2.setAbbreviation("MIG");

        memberList = Arrays.asList(member1, member2);
    }

    @Test
    void testGetMembers_Success() throws ApiException {
        when(membersApi.getMember()).thenReturn(memberList);

        List<MemberDto> result = memberService.getMembers();

        assertEquals(2, result.size());
        assertEquals(memberList, result);
        verify(membersApi, times(1)).getMember();
    }

    @Test
    void testGetMembers_ThrowsApiException() throws ApiException {
        when(membersApi.getMember()).thenThrow(new ApiException("API Error details"));

        MigrationException exception = assertThrows(MigrationException.class, () -> {
            memberService.getMembers();
        });

        assertEquals(400, exception.getError().status().value());
        assertEquals("API Error details", exception.getError().message());
        verify(membersApi, times(1)).getMember();
    }

    @Test
    void testGetMemberIdBy_MatchFound() throws ApiException {
        when(membersApi.getMember()).thenReturn(memberList);

        Long id = memberService.getMemberIdBy("MIG");

        assertEquals(20L, id);
        verify(membersApi, times(1)).getMember();
    }

    @Test
    void testGetMemberIdBy_NoMatchFound_ThrowsMigrationException() throws ApiException {
        when(membersApi.getMember()).thenReturn(memberList);
        String unknownAbbreviation = "XYZ";

        MigrationException exception = assertThrows(MigrationException.class, () -> {
            memberService.getMemberIdBy(unknownAbbreviation);
        });

        assertEquals(404, exception.getError().status().value());
        assertEquals("Member with abbreviation XYZ not found", exception.getError().message());
        verify(membersApi, times(1)).getMember();
    }

    @Test
    void testGetMemberIdBy_EmptyList_ThrowsMigrationException() throws ApiException {
        when(membersApi.getMember()).thenReturn(Collections.emptyList());

        MigrationException exception = assertThrows(MigrationException.class, () -> {
            memberService.getMemberIdBy("PUZ");
        });

        assertEquals(404, exception.getError().status().value());
        assertEquals("Member with abbreviation PUZ not found", exception.getError().message());
    }
}