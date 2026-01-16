package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.mapper.CalculationMapper;
import ch.puzzle.pcts.mapper.MemberMapper;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerIT {

    @MockitoBean
    private MemberBusinessService service;

    @MockitoBean
    private MemberMapper mapper;

    @MockitoBean
    private CalculationMapper calculationMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/members";

    @DisplayName("Should successfully get all members")
    @Test
    void shouldGetAllMembers() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(MEMBER_1));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(MEMBER_1_DTO));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(MEMBER_1_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get member by id")
    @Test
    void shouldGetMemberById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(MEMBER_1);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(MEMBER_1_DTO);

        mvc
                .perform(get(BASEURL + "/" + MEMBER_1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(MEMBER_1_DTO, "$"));

        verify(service, times(1)).getById(MEMBER_1_ID);
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully create new member")
    @Test
    void shouldCreateNewMember() throws Exception {
        BDDMockito.given(mapper.fromDto(any(MemberInputDto.class))).willReturn(MEMBER_1);
        BDDMockito.given(service.create(any(Member.class))).willReturn(MEMBER_1);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(MEMBER_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(MEMBER_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(MEMBER_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(MemberInputDto.class));
        verify(service, times(1)).create(any(Member.class));
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully update member")
    @Test
    void shouldUpdateMember() throws Exception {
        BDDMockito.given(mapper.fromDto(any(MemberInputDto.class))).willReturn(MEMBER_1);
        BDDMockito.given(service.update(any(Long.class), any(Member.class))).willReturn(MEMBER_1);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(MEMBER_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + MEMBER_1_ID)
                        .content(jsonMapper.writeValueAsString(MEMBER_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(MEMBER_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(MemberInputDto.class));
        verify(service, times(1)).update(any(Long.class), any(Member.class));
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully delete member")
    @Test
    void shouldDeleteMember() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + MEMBER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }

    @DisplayName("Should successfully get calculations of member with optional roleId")
    @Test
    void shouldGetCalculationsByMemberId() throws Exception {
        Long memberId = 1L;
        Long roleId = 2L;

        Calculation calculation = mock(Calculation.class);
        CalculationDto calculationDto = mock(CalculationDto.class);

        BDDMockito
                .given(service.getAllCalculationsByMemberIdAndRoleId(memberId, roleId))
                .willReturn(List.of(calculation));

        BDDMockito.given(calculationMapper.toDto(anyList())).willReturn(List.of(calculationDto));

        mvc
                .perform(get(BASEURL + "/" + memberId + "/calculations")
                        .param("roleId", String.valueOf(roleId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(service, times(1)).getAllCalculationsByMemberIdAndRoleId(memberId, roleId);
        verify(calculationMapper, times(1)).toDto(anyList());
    }

    @DisplayName("Should successfully get calculations of member without roleId")
    @Test
    void shouldGetCalculationsByMemberIdWithoutRoleId() throws Exception {
        Long memberId = 1L;

        Calculation calculation = mock(Calculation.class);
        CalculationDto calculationDto = mock(CalculationDto.class);

        BDDMockito
                .given(service.getAllCalculationsByMemberIdAndRoleId(memberId, null))
                .willReturn(List.of(calculation));

        BDDMockito.given(calculationMapper.toDto(anyList())).willReturn(List.of(calculationDto));

        mvc
                .perform(get(BASEURL + "/" + memberId + "/calculations")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(service, times(1)).getAllCalculationsByMemberIdAndRoleId(memberId, null);
        verify(calculationMapper, times(1)).toDto(anyList());
    }
}
