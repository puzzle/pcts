package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.INVALID_ID;
import static ch.puzzle.pcts.util.TestData.MEMBER_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.MEMBER_1_OVERVIEW_DTO;
import static ch.puzzle.pcts.util.TestDataModels.MEMBER_1_OVERVIEWS;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.mapper.MemberOverviewMapper;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.security.SpringSecurityConfig;
import ch.puzzle.pcts.service.business.MemberOverviewBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @DisplayName("Should successfully get member overview by member id")
    @Test
    void shouldGetMemberOverviewById() throws Exception {
        List<MemberOverview> memberOverviews = MEMBER_1_OVERVIEWS;
        MemberOverviewDto expectedDto = MEMBER_1_OVERVIEW_DTO;

        when(service.getById(anyLong())).thenReturn(memberOverviews);
        when(mapper.toDto(memberOverviews)).thenReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + MEMBER_1_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(MEMBER_1_ID);
        verify(mapper, times(1)).toDto(memberOverviews);
    }

    @DisplayName("Should return 404 with NOT_FOUND error when member does not exist")
    @Test
    void shouldReturn404WhenMemberNotFound() throws Exception {
        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY, "Member", FieldKey.FIELD, "id", FieldKey.IS, INVALID_ID.toString());

        GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

        when(service.getById(INVALID_ID)).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of(error)));

        mvc
                .perform(get(BASEURL + "/" + INVALID_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].key").value("NOT_FOUND"))
                .andExpect(jsonPath("$[0].values.ENTITY").value("Member"))
                .andExpect(jsonPath("$[0].values.FIELD").value("id"))
                .andExpect(jsonPath("$[0].values.IS").value(INVALID_ID.toString()));

        verify(service).getById(INVALID_ID);
        verifyNoInteractions(mapper);
    }
}
