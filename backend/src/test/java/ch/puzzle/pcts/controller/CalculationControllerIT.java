package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.mapper.CalculationMapper;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.CalculationBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CalculationController.class)
class CalculationControllerIT {

    private static final String BASEURL = "/api/v1/calculations";
    private static final Long ID = 1L;
    private static final Long ORGANISATION_UNIT_ID = 1L;
    private static final Long ROLE_ID = 1L;

    private final LocalDate commonDate = LocalDate.of(2020, 1, 1);

    @MockitoBean
    private CalculationBusinessService businessService;
    @MockitoBean
    private CalculationMapper mapper;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;
    private Calculation calculation;
    private CalculationInputDto inputDto;
    private CalculationDto expectedDto;

    @BeforeEach
    void setUp() {

        Role role = new Role(ROLE_ID, "Developer", true);
        OrganisationUnit ou = new OrganisationUnit(ORGANISATION_UNIT_ID, "/dev");
        Member member = Member.Builder
                .builder()
                .withId(ID)
                .withFirstName("Alex")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation("AM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(ou)
                .build();

        calculation = Calculation.Builder
                .builder()
                .withId(ID)
                .withMember(member)
                .withRole(role)
                .withState(CalculationState.ACTIVE)
                .withPublicationDate(commonDate)
                .withPublicizedBy("System")
                .withDegrees(List.of())
                .withExperiences(List.of())
                .withCertificates(List.of())
                .withPoints(BigDecimal.TEN)
                .build();

        inputDto = new CalculationInputDto(ID,
                                           CalculationState.ACTIVE,
                                           ROLE_ID,
                                           List.of(),
                                           List.of(),
                                           List.of(),
                                           List.of());

        RoleDto roleDto = new RoleDto(ROLE_ID, "Developer", true);
        OrganisationUnitDto ouDto = new OrganisationUnitDto(ORGANISATION_UNIT_ID, "/dev");

        MemberDto memberDto = new MemberDto(ID,
                                            "Alex",
                                            "Miller",
                                            EmploymentState.MEMBER,
                                            "AM",
                                            commonDate,
                                            commonDate,
                                            ouDto);

        expectedDto = new CalculationDto(ID,
                                         memberDto,
                                         roleDto,
                                         CalculationState.ACTIVE,
                                         commonDate,
                                         "System",
                                         BigDecimal.TEN,
                                         List.of(mock(CertificateCalculationDto.class)),
                                         List.of(mock(LeadershipExperienceCalculationDto.class)),
                                         List.of(mock(DegreeCalculationDto.class)),
                                         List.of(mock(ExperienceCalculationDto.class)));

    }

    @DisplayName("Should successfully get calculation by ID")
    @Test
    void shouldGetCalculationById() throws Exception {
        given(businessService.getById(ID)).willReturn(calculation);
        given(mapper.toDto(any(Calculation.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/{id}", ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(businessService, times(1)).getById(ID);
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }

    @DisplayName("Should successfully create new calculation")
    @Test
    void shouldCreateCalculation() throws Exception {
        given(mapper.fromDto(any(CalculationInputDto.class))).willReturn(calculation);
        given(businessService.create(any(Calculation.class))).willReturn(calculation);
        given(mapper.toDto(any(Calculation.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CalculationInputDto.class));
        verify(businessService, times(1)).create(any(Calculation.class));
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }

    @DisplayName("Should successfully update calculation")
    @Test
    void shouldUpdateCalculation() throws Exception {
        given(mapper.fromDto(any(CalculationInputDto.class))).willReturn(calculation);
        given(businessService.update(eq(ID), any(Calculation.class))).willReturn(calculation);
        given(mapper.toDto(any(Calculation.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/{id}", ID)
                        .content(jsonMapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CalculationInputDto.class));
        verify(businessService, times(1)).update(eq(ID), any(Calculation.class));
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }
}
