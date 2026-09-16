package ch.puzzle.pctsmigration.certificates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.api.LeadershipExperienceService;
import ch.puzzle.pctsmigration.api.LeadershipExperienceTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.leadershipexperience.LeadershipExperienceAiResultDto;
import ch.puzzle.pctsmigration.leadershipexperience.LeadershipExperienceContextModel;
import ch.puzzle.pctsmigration.leadershipexperience.LeadershipExperienceExtractionPipeline;
import ch.puzzle.pctsmigration.leadershipexperience.LeadershipExperienceWrapper;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.openapitools.client.model.LeadershipExperienceTypeDto;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceExtractionPipelineTest {

    @Mock
    private LeadershipExperienceService leadershipExperienceService;

    @Mock
    private LeadershipExperienceTypeService leadershipExperienceTypeService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private LeadershipExperienceExtractionPipeline pipeline;

    @Test
    @DisplayName("fetchContext should return the current date and three expected kinds")
    void fetchContext_returnsCurrentDateAndKinds() {
        LeadershipExperienceContextModel context = pipeline.fetchContext();

        assertThat(context.currentDate()).isEqualTo(LocalDate.now());
        assertThat(context.kinds())
                .containsExactlyInAnyOrder(LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.MILITARY_FUNCTION,
                                           LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.LEADERSHIP_TRAINING,
                                           LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.YOUTH_AND_SPORT);
    }

    @Test
    @DisplayName("systemPrompt should include the date and categories from the context")
    void systemPrompt_includesDateAndCategoriesFromContext() {
        LocalDate date = LocalDate.of(2026, 8, 15);
        List<LeadershipExperienceTypeDto.LeadershipExperienceKindEnum> kinds = List
                .of(LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.MILITARY_FUNCTION);
        LeadershipExperienceContextModel context = new LeadershipExperienceContextModel(date, kinds);

        String prompt = pipeline.systemPrompt(context);

        assertThat(prompt).contains("Current date: 2026-08-15");
        assertThat(prompt).contains("MILITARY_FUNCTION");
    }

    @Test
    @DisplayName("odsSheetParseConfig should match correct sheet names and configurations")
    void odsSheetParseConfig_configuresSheetNamesCorrectly() {
        OdsParseConfig config = pipeline.odsSheetParseConfig();

        assertThat(config.tableNameConvention().test("M-Mitarbeiter")).isTrue();
        assertThat(config.tableNameConvention().test("M")).isTrue();
        assertThat(config.tableNameConvention().test("Master")).isFalse();
        assertThat(config.tableNameConvention().test("Zertifikate")).isFalse();

        assertThat(config.startMarker()).isEqualTo("Führungserfahrung (nur bei M-Rollen)");
        assertThat(config.shouldCutOfCalcRow()).isTrue();
    }

    @Test
    @DisplayName("entityClass should return LeadershipExperienceWrapper.class")
    void entityClass_returnsLeadershipExperienceWrapperClass() {
        assertThat(pipeline.entityClass()).isEqualTo(LeadershipExperienceWrapper.class);
    }

    @Test
    @DisplayName("Extract the abbreviation and map to closest leadership experience type based on Levenshtein distance")
    void mapToDto_withValidData_mapsToDtoAndFindsClosestType() {
        String filename = "aw_leadershipexperience.ods";
        Long expectedMemberId = 42L;

        LeadershipExperienceAiResultDto aiResult = mock(LeadershipExperienceAiResultDto.class);
        when(aiResult.name()).thenReturn("Offizier Mil");
        when(aiResult.comment()).thenReturn("Gute Führung");

        LeadershipExperienceWrapper wrapper = mock(LeadershipExperienceWrapper.class);
        when(wrapper.items()).thenReturn(List.of(aiResult));

        LeadershipExperienceTypeDto wrongType = mock(LeadershipExperienceTypeDto.class);
        when(wrongType.getName()).thenReturn("J+S Leiter");

        LeadershipExperienceTypeDto correctClosestType = mock(LeadershipExperienceTypeDto.class);
        when(correctClosestType.getName()).thenReturn("Offizier Militär");
        when(correctClosestType.getId()).thenReturn(15L);

        when(memberService.getMemberIdBy("AW")).thenReturn(expectedMemberId);
        when(leadershipExperienceTypeService.getLeadershipExperienceTypes())
                .thenReturn(List.of(wrongType, correctClosestType));

        List<LeadershipExperienceInputDto> result = pipeline.mapToDto(filename, wrapper);

        assertThat(result).hasSize(1);
        LeadershipExperienceInputDto dto = result.getFirst();

        assertThat(dto.getMemberId()).isEqualTo(expectedMemberId);
        assertThat(dto.getLeadershipExperienceTypeId()).isEqualTo(15L);
        assertThat(dto.getComment()).isEqualTo("Gute Führung");

        verify(memberService).getMemberIdBy("AW");
        verify(leadershipExperienceTypeService).getLeadershipExperienceTypes();
    }

    @Test
    @DisplayName("Should delegate creation to the LeadershipExperienceService")
    void create_delegatesToLeadershipExperienceService() {
        List<LeadershipExperienceInputDto> dtos = List.of(new LeadershipExperienceInputDto());

        pipeline.create(dtos);

        verify(leadershipExperienceService, times(1)).create(eq(dtos));
    }
}