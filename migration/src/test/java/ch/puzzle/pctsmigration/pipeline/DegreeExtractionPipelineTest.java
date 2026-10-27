package ch.puzzle.pctsmigration.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import ch.puzzle.pctsmigration.api.DegreeService;
import ch.puzzle.pctsmigration.api.DegreeTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.degree.DegreeAiResultDto;
import ch.puzzle.pctsmigration.degree.DegreeContextModel;
import ch.puzzle.pctsmigration.degree.DegreeExtractionPipeline;
import ch.puzzle.pctsmigration.degree.DegreeWrapper;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import ch.puzzle.pctsmigration.service.MatchingService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.DegreeInputDto;
import org.openapitools.client.model.DegreeTypeDto;

@ExtendWith(MockitoExtension.class)
class DegreeExtractionPipelineTest {

    @Mock
    private DegreeService degreeService;

    @Mock
    private DegreeTypeService degreeTypeService;

    @Mock
    private MemberService memberService;

    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private DegreeExtractionPipeline degreeExtractionPipeline;

    @Test
    @DisplayName("fetchContext should return the current date")
    void fetchContext_returnsCurrentDate() {
        DegreeContextModel context = degreeExtractionPipeline.fetchContext();

        assertThat(context.currentDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("systemPrompt should include the date from the context")
    void systemPrompt_includesDateFromContext() {
        LocalDate date = LocalDate.of(2026, 8, 15);
        DegreeContextModel context = new DegreeContextModel(date);

        String prompt = degreeExtractionPipeline.systemPrompt(context);

        assertThat(prompt).contains("2026-08-15");
    }

    @Test
    @DisplayName("odsSheetParseConfig should match correct sheet names and configurations")
    void odsSheetParseConfig_configuresSheetNamesCorrectly() {
        OdsParseConfig config = degreeExtractionPipeline.odsSheetParseConfig();

        assertThat(config.tableNameConvention().test("M-Mitarbeiter")).isTrue();
        assertThat(config.tableNameConvention().test("M")).isTrue();
        assertThat(config.tableNameConvention().test("Master")).isFalse();
        assertThat(config.tableNameConvention().test("Zertifikate")).isFalse();
        assertThat(config.tableNameConvention().test("Berufserfahrungen")).isFalse();
        assertThat(config.tableNameConvention().test("T3 smth (alt)")).isFalse();

        assertThat(config.startMarker()).isEqualTo("Ausbildung");
        assertThat(config.shouldCutOfCalcRow()).isTrue();
    }

    @Test
    @DisplayName("entityClass should return DegreeWrapper.class")
    void entityClass_returnsDegreeWrapperClass() {
        assertThat(degreeExtractionPipeline.entityClass()).isEqualTo(DegreeWrapper.class);
    }

    @Test
    @DisplayName("Extract the abbreviation and map to closest degree type based on Levenshtein distance")
    void mapToDto_withValidData_mapsToDtoAndFindsClosestType() {
        String filename = "aw_leadershipexperience.ods";
        Long expectedMemberId = 42L;

        DegreeAiResultDto aiResult = mock(DegreeAiResultDto.class);
        when(aiResult.name()).thenReturn("Lehr");
        when(aiResult.comment()).thenReturn("Guter Schnitt");

        DegreeWrapper wrapper = mock(DegreeWrapper.class);
        when(wrapper.items()).thenReturn(List.of(aiResult));

        DegreeTypeDto wrongType = mock(DegreeTypeDto.class);

        DegreeTypeDto correctClosestType = mock(DegreeTypeDto.class);
        when(correctClosestType.getId()).thenReturn(15L);

        when(memberService.getMemberIdBy("AW")).thenReturn(expectedMemberId);
        when(degreeTypeService.getDegreeTypes()).thenReturn(List.of(wrongType, correctClosestType));

        when(matchingService.match(anyList(), anyString(), any())).thenReturn(correctClosestType);

        List<DegreeInputDto> result = degreeExtractionPipeline.mapToDto(filename, wrapper);

        assertThat(result).hasSize(1);
        DegreeInputDto dto = result.getFirst();

        assertThat(dto.getMemberId()).isEqualTo(expectedMemberId);
        assertThat(dto.getTypeId()).isEqualTo(15L);
        assertThat(dto.getComment()).isEqualTo("Guter Schnitt");

        verify(memberService).getMemberIdBy("AW");
        verify(degreeTypeService).getDegreeTypes();
    }

    @Test
    @DisplayName("Should delegate creation to the degreeService")
    void create_delegatesToLeadershipExperienceService() {
        List<DegreeInputDto> dtos = List.of(new DegreeInputDto());

        degreeExtractionPipeline.create(dtos);

        verify(degreeService, times(1)).create(eq(dtos));
    }
}
