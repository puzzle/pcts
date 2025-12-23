package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.MEMBER_1_OVERVIEWS;
import static ch.puzzle.pcts.util.TestData.MEMBER_1_OVERVIEW_DTO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.mapper.MemberOverviewMapper;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.business.MemberOverviewBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberOverviewController.class)
class MemberOverviewControllerIT {

    @MockitoBean
    private MemberOverviewBusinessService service;

    @MockitoBean
    private MemberOverviewMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static final String BASEURL = "/api/v1/member-overviews";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @DisplayName("Should successfully get member overview by member id")
    @Test
    void shouldGetMemberOverviewById() throws Exception {
        Long memberId = 1L;

        List<MemberOverview> memberOverviews = MEMBER_1_OVERVIEWS;
        MemberOverviewDto expectedDto = MEMBER_1_OVERVIEW_DTO;

        BDDMockito.given(service.getById(anyLong())).willReturn(memberOverviews);
        BDDMockito.given(mapper.toDto(memberOverviews)).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + memberId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(memberId);
        verify(mapper, times(1)).toDto(memberOverviews);
    }
}
