package ch.puzzle.pcts.service.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.service.JwtService;
import ch.puzzle.pcts.util.IT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@IT
class MemberLoginLoopRegressionIT {

    @Autowired
    MemberBusinessService memberBusinessService;

    @MockitoBean
    JwtService jwtService;

    @DisplayName("Should not return logged in member when member is deleted")
    @Test
    void shouldNotReturnLoggedInMemberWhenIsDeleted() {
        when(jwtService.getLdapName()).thenReturn("mtest4");

        PCTSException exception = assertThrows(PCTSException.class, () -> memberBusinessService.getLoggedInMember());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @DisplayName("Should return logged in member when member is not deleted")
    @Test
    void shouldReturnLoggedInMemberWhenIsNotDeleted() {
        when(jwtService.getLdapName()).thenReturn("mtest1");

        assertThat(memberBusinessService.getLoggedInMember().getLdapName()).isEqualTo("mtest1");
    }
}
