package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.DegreeMapper;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import java.math.BigDecimal;
import java.util.Date;
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

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(DegreeController.class)
public class DegreeControllerIT {
    @MockitoBean
    private DegreeBusinessService businessService;

    @MockitoBean
    private DegreeMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static final String BASEURL = "/api/v1/degree";

    private Long id;

    private Degree degree;
    private DegreeDto degreeDto;
    private DegreeInputDto degreeInputDto;

    private DegreeTypeDto degreeTypeDto;
    private DegreeType degreeType;

    private Member member;
    private MemberDto memberDto;

    private final Date commonDate = new Date(0L);
    @Autowired
    private DegreeMapper degreeMapper;

    @BeforeEach
    public void setUp() {
        id = 1L;
        degreeType = new DegreeType(id,
                                    "Degree Type 1",
                                    new BigDecimal("3.0"),
                                    new BigDecimal("2.0"),
                                    new BigDecimal("1.0"));
        degreeTypeDto = new DegreeTypeDto(id,
                                          "Degree Type 1",
                                          new BigDecimal("3.0"),
                                          new BigDecimal("2.0"),
                                          new BigDecimal("1.0"));

        OrganisationUnit organisationUnit = new OrganisationUnit(id, "/bbt");
        OrganisationUnitDto organisationUnitDto = new OrganisationUnitDto(id, "/bbt");

        member = Member.Builder
                .builder()
                .withId(id)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();
        memberDto = new MemberDto(id,
                                  "Susi",
                                  "Miller",
                                  EmploymentState.APPLICANT,
                                  "SM",
                                  commonDate,
                                  commonDate,
                                  organisationUnitDto);

        degreeInputDto = new DegreeInputDto(id,
                                            "Degree 1",
                                            "Institution 1",
                                            true,
                                            id,
                                            commonDate,
                                            commonDate,
                                            "Comment 1");

        degree = Degree.Builder
                .builder()
                .withId(id)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution 1")
                .withCompleted(true)
                .withType(degreeType)
                .withStartDate(commonDate)
                .withEndDate(commonDate)
                .withComment("Comment 1")
                .build();

        degreeDto = new DegreeDto(id,
                                  memberDto,
                                  "Degree 1",
                                  "Institution 1",
                                  true,
                                  degreeTypeDto,
                                  commonDate,
                                  commonDate,
                                  "Comment 1");
    }
    @DisplayName("Should successfully get degree by id")
    @Test
    void shouldSuccessfullyGetDegreeById() throws Exception {
        BDDMockito.given(businessService.getById(id)).willReturn(degree);
        BDDMockito.given(degreeMapper.toDto(any(Degree.class))).willReturn(degreeDto);

        mvc
                .perform(get(BASEURL + "/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Degree 1"))
                .andExpect(jsonPath("$.institution").value("Institution 1"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.comment").value("Comment 1"))
                // nested member fields
                .andExpect(jsonPath("$.member.id").value(id))
                .andExpect(jsonPath("$.member.firstName").value("Susi"))
                .andExpect(jsonPath("$.member.lastName").value("Miller"))
                .andExpect(jsonPath("$.member.employmentState").value("APPLICANT"))
                // nested type fields
                .andExpect(jsonPath("$.type.id").value(id))
                .andExpect(jsonPath("$.type.name").value("Degree Type 1"));

        verify(businessService, times(1)).getById(id);
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

}
