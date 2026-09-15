package ch.puzzle.pctsmigration.certificates;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.api.CertificateService;
import ch.puzzle.pctsmigration.api.CertificateTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;

@ExtendWith(MockitoExtension.class)
class CertificateExtractionPipelineTest {

    @Mock
    private CertificateTypeService certificateTypeService;

    @Mock
    private MemberService memberService;

    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private CertificateExtractionPipeline pipeline;

    @Test
    @DisplayName("fetchContext should return the current date")
    void fetchContext_returnsCurrentDate() {
        CertificateContextModel context = pipeline.fetchContext();

        assertThat(context.currentDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("systemPrompt should include the date from the context")
    void systemPrompt_includesDateFromContext() {
        LocalDate date = LocalDate.of(2026, 5, 10);
        CertificateContextModel context = new CertificateContextModel(date);

        String prompt = pipeline.systemPrompt(context);

        assertThat(prompt).contains("Current date: 2026-05-10");
    }

    @Test
    @DisplayName("odsSheetParseConfig should match correct sheet names and configurations")
    void odsSheetParseConfig_configuresSheetNamesCorrectly() {
        OdsParseConfig config = pipeline.odsSheetParseConfig();

        assertThat(config.tableNameConvention().apply("Zertifikat")).isTrue();
        assertThat(config.tableNameConvention().apply("Zertifikate")).isTrue();
        assertThat(config.tableNameConvention().apply("InvalidSheet")).isFalse();

        assertThat(config.startMarker()).isNull();
        assertThat(config.shouldCutOfCalcRow()).isFalse();
    }

    @Test
    @DisplayName("entityClass should return CertificateWrapper.class")
    void entityClass_returnsCertificateWrapperClass() {
        assertThat(pipeline.entityClass()).isEqualTo(CertificateWrapper.class);
    }

    @Test
    @DisplayName("Extract the abbreviation from the filename and match it to the most similar certificate based on Levenshtein distance")
    void mapToDto_withValidData_mapsToDtoAndFindsClosestCertificateType() {
        String filename = "aw_zertifikate.ods";
        Long expectedMemberId = 42L;

        CertificateAiResultDto aiResult = mock(CertificateAiResultDto.class);
        when(aiResult.name()).thenReturn("Scrum Mstr");
        when(aiResult.comment()).thenReturn("Sehr gut");
        when(aiResult.completedAt()).thenReturn(LocalDate.of(2025, 1, 15));

        CertificateWrapper wrapper = mock(CertificateWrapper.class);
        when(wrapper.items()).thenReturn(List.of(aiResult));

        CertificateTypeDto wrongType = mock(CertificateTypeDto.class);
        when(wrongType.getName()).thenReturn("Java Developer");

        CertificateTypeDto correctClosestType = mock(CertificateTypeDto.class);
        when(correctClosestType.getName()).thenReturn("Scrum Master");
        when(correctClosestType.getId()).thenReturn(12L);

        when(memberService.getMemberIdBy("AW")).thenReturn(expectedMemberId);
        when(certificateTypeService.getCertificateTypes()).thenReturn(List.of(wrongType, correctClosestType));

        List<CertificateInputDto> result = pipeline.mapToDto(filename, wrapper);

        assertThat(result).hasSize(1);
        CertificateInputDto dto = result.getFirst();

        assertThat(dto.getMemberId()).isEqualTo(expectedMemberId);
        assertThat(dto.getValidUntil()).isNull();
        assertThat(dto.getComment()).isEqualTo("Sehr gut");
        assertThat(dto.getCompletedAt()).isEqualTo(LocalDate.of(2025, 1, 15));
        assertThat(dto.getCertificateTypeId()).isEqualTo(12L);

        verify(memberService).getMemberIdBy("AW");
        verify(certificateTypeService).getCertificateTypes();
    }

    @Test
    @DisplayName("Should delegate creation to the CertificateService")
    void create_delegatesToCertificateService() {
        List<CertificateInputDto> dtos = List.of(new CertificateInputDto());

        pipeline.create(dtos);

        verify(certificateService, times(1)).create(eq(dtos));
    }
}
