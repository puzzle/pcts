package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.mapper.ExperienceMapper;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.ResultMatcher;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExperienceController.class)
class ExperienceControllerIT {

    private static final String BASEURL = "/api/v1/experiences";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final LocalDate startDate = LocalDate.of(2020, 1, 1);
    private final LocalDate endDate = startDate.plusDays(30);
    @MockitoBean
    private ExperienceBusinessService service;
    @MockitoBean
    private ExperienceMapper mapper;
    @Autowired
    private MockMvc mvc;
    private Experience experience;
    private ExperienceInputDto requestDto;
    private ExperienceDto expectedDto;
    private Member member;
    private MemberDto memberDto;
    private ExperienceType type;
    private ExperienceTypeDto typeDto;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 1L;

        member = Member.Builder.builder().withId(2L).build();
        memberDto = new MemberDto(2L, "Jane", "Doe", null, null, null, null, null);

        type = new ExperienceType(10L, "Management", new BigDecimal("10"), new BigDecimal("3"), new BigDecimal("8"));
        typeDto = new ExperienceTypeDto(10L,
                                        "Management",
                                        new BigDecimal("10"),
                                        new BigDecimal("3"),
                                        new BigDecimal("8"));

        experience = new Experience.Builder()
                .withId(id)
                .withMember(member)
                .withName("Developer")
                .withEmployer("Puzzle ITC")
                .withPercent(80)
                .withType(type)
                .withComment("Worked on backend")
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();

        expectedDto = new ExperienceDto(id,
                                        memberDto,
                                        "Developer",
                                        "Puzzle ITC",
                                        80,
                                        typeDto,
                                        "Worked on backend",
                                        startDate,
                                        endDate);

        requestDto = new ExperienceInputDto(member
                .getId(), "Developer", "Puzzle ITC", 80, type.getId(), "Worked on backend", startDate, endDate);
    }

    @DisplayName("Should successfully get all experiences")
    @Test
    void shouldGetAllExperiences() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(experience));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(matchesExperienceDto(expectedDto, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get experience by id")
    @Test
    void shouldGetExperienceById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(experience);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + id).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(matchesExperienceDto(expectedDto, "$"));

        verify(service, times(1)).getById(id);
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully create new experience")
    @Test
    void shouldCreateExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceInputDto.class))).willReturn(experience);
        BDDMockito.given(service.create(any(Experience.class))).willReturn(experience);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(matchesExperienceDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceInputDto.class));
        verify(service, times(1)).create(any(Experience.class));
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully update experience")
    @Test
    void shouldUpdateExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceInputDto.class))).willReturn(experience);
        BDDMockito.given(service.update(anyLong(), any(Experience.class))).willReturn(experience);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(matchesExperienceDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceInputDto.class));
        verify(service, times(1)).update(anyLong(), any(Experience.class));
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully delete experience")
    @Test
    void shouldDeleteExperience() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + id).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(anyLong());
    }

    public static ResultMatcher matchesExperienceDto(ExperienceDto dto, String jsonPrefix) {
        return result -> {
            jsonPath(jsonPrefix + ".id").value(dto.id()).match(result);
            jsonPath(jsonPrefix + ".name").value(dto.name()).match(result);
            jsonPath(jsonPrefix + ".employer").value(dto.employer()).match(result);
            jsonPath(jsonPrefix + ".percent").value(dto.percent()).match(result);
            jsonPath(jsonPrefix + ".comment").value(dto.comment()).match(result);
            jsonPath(jsonPrefix + ".startDate").value(dto.startDate().toString()).match(result);
            jsonPath(jsonPrefix + ".endDate").value(dto.endDate().toString()).match(result);
            jsonPath(jsonPrefix + ".member.id").value(dto.member().id()).match(result);
            jsonPath(jsonPrefix + ".member.firstName").value(dto.member().firstName()).match(result);
            jsonPath(jsonPrefix + ".member.lastName").value(dto.member().lastName()).match(result);
            jsonPath(jsonPrefix + ".member.employmentState").value(dto.member().employmentState()).match(result);
            jsonPath(jsonPrefix + ".type.id").value(dto.type().id()).match(result);
            jsonPath(jsonPrefix + ".type.name").value(dto.type().name()).match(result);
        };
    }
}
