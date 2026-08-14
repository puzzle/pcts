// package ch.puzzle.pctsmigration.certificates;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
//
// import ch.puzzle.pctsmigration.api.CertificateService;
// import ch.puzzle.pctsmigration.api.CertificateTypeService;
// import ch.puzzle.pctsmigration.api.MemberService;
// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.Collections;
// import java.util.List;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.openapitools.client.ApiException;
// import org.openapitools.client.model.CertificateInputDto;
// import org.openapitools.client.model.CertificateTypeDto;
//
// @ExtendWith(MockitoExtension.class)
// class CertificateExtractionPipelineTest {
//
// @Mock
// private CertificateTypeService certificateTypeService;
//
// @Mock
// private MemberService memberService;
//
// @Mock
// private CertificateService certificateService;
//
// @InjectMocks
// private CertificateExtractionPipeline pipeline;
//
// @Test
// @DisplayName("fetchContext should return the current date")
// void fetchContext_returnsCurrentDate() {
// CertificateContextModel context = pipeline.fetchContext();
//
// assertThat(context.currentDate()).isEqualTo(LocalDate.now());
// }
//
// @Test
// @DisplayName("systemPrompt should include the date from the context")
// void systemPrompt_includesDateFromContext() {
// LocalDate date = LocalDate.of(2026, 5, 10);
// CertificateContextModel context = new CertificateContextModel(date);
//
// String prompt = pipeline.systemPrompt(context);
//
// assertThat(prompt).contains("Current date: 2026-05-10");
// }
//
// @Test
// @DisplayName("entityClass should return CertificateWrapper.class")
// void entityClass_returnsCertificateWrapperClass() {
// assertThat(pipeline.entityClass()).isEqualTo(CertificateWrapper.class);
// }
//
// @Test
// @DisplayName("Should return an empty list if `wrapper` or `items` are null")
// void mapToDto_whenWrapperOrItemsNull_returnsEmptyList() {
// assertThat(pipeline.mapToDto("test.ods", null)).isEmpty();
//
// CertificateWrapper emptyWrapper = mock(CertificateWrapper.class);
// when(emptyWrapper.items()).thenReturn(null);
//
// assertThat(pipeline.mapToDto("test.ods", emptyWrapper)).isEmpty();
// }
//
// @Test
// @DisplayName("Extract the abbreviation from the filename and match it to the
// most similar certificate based on Levenshtein distance")
// void mapToDto_withValidData_mapsToDtoAndFindsClosestCertificateType() throws
// Exception {
// String filename = "aw_zertifikate.ods";
// Long expectedMemberId = 42L;
//
// CertificateAiResultDto aiResult = mock(CertificateAiResultDto.class);
// when(aiResult.name()).thenReturn("Scrum Mstr");
// when(aiResult.points()).thenReturn(new BigDecimal("10.0"));
// when(aiResult.comment()).thenReturn("Sehr gut");
// when(aiResult.completedAt()).thenReturn(LocalDate.of(2025, 1, 15));
//
// CertificateWrapper wrapper = mock(CertificateWrapper.class);
// when(wrapper.items()).thenReturn(List.of(aiResult));
//
// CertificateTypeDto wrongType = new CertificateTypeDto();
// wrongType.setName("Java Developer");
// wrongType.setPoints(new BigDecimal("5.0"));
//
// CertificateTypeDto correctClosestType = new CertificateTypeDto();
// correctClosestType.setName("Scrum Master");
// correctClosestType.setPoints(new BigDecimal("10.0"));
//
// when(memberService.getMemberIdBy("AW")).thenReturn(expectedMemberId);
// when(certificateTypeService.getCertificateTypes()).thenReturn(List.of(wrongType,
// correctClosestType));
//
// List<CertificateInputDto> result = pipeline.mapToDto(filename, wrapper);
//
// assertThat(result).hasSize(1);
// CertificateInputDto dto = result.getFirst();
//
// assertThat(dto.getMemberId()).isEqualTo(expectedMemberId);
// assertThat(dto.getValidUntil()).isNull();
// assertThat(dto.getComment()).isEqualTo("Sehr gut");
// assertThat(dto.getCompletedAt()).isEqualTo(LocalDate.of(2025, 1, 15));
//
// verify(memberService).getMemberIdBy("AW");
// }
//
// @Test
// @DisplayName("Pass null as a suffix if the filename does not contain an
// underscore")
// void mapToDto_whenFilenameHasNoUnderscore_passesNullToMemberService() throws
// Exception {
// String filename = "ohneunterstrich.ods";
// CertificateAiResultDto aiResult = mock(CertificateAiResultDto.class);
// when(aiResult.name()).thenReturn("Test");
// when(aiResult.points()).thenReturn(BigDecimal.ONE);
//
// CertificateWrapper wrapper = mock(CertificateWrapper.class);
// when(wrapper.items()).thenReturn(List.of(aiResult));
//
// when(memberService.getMemberIdBy(null)).thenReturn(100L);
// when(certificateTypeService.getCertificateTypes()).thenReturn(Collections.emptyList());
//
// List<CertificateInputDto> result = pipeline.mapToDto(filename, wrapper);
//
// assertThat(result).hasSize(1);
// assertThat(result.getFirst().getMemberId()).isEqualTo(100L);
// verify(memberService).getMemberIdBy(null);
// }
//
// @Test
// @DisplayName("Should throw an IllegalStateException if an ApiException occurs
// during mapping")
// void mapToDto_whenApiExceptionOccurs_throwsIllegalStateException() throws
// Exception {
// String filename = "AW_test.ods";
// CertificateAiResultDto aiResult = mock(CertificateAiResultDto.class);
//
// CertificateWrapper wrapper = mock(CertificateWrapper.class);
// when(wrapper.items()).thenReturn(List.of(aiResult));
//
// when(memberService.getMemberIdBy(any())).thenThrow(new ApiException("API
// Error"));
//
// assertThatThrownBy(() -> pipeline.mapToDto(filename, wrapper))
// .isInstanceOf(IllegalStateException.class)
// .hasMessageContaining("Failed to map AI result to DTO: AW")
// .hasCauseInstanceOf(ApiException.class);
// }
//
// @Test
// @DisplayName("Should delegate creation to the CertificateService")
// void create_delegatesToCertificateService() throws Exception {
// List<CertificateInputDto> dtos = List.of(new CertificateInputDto());
//
// pipeline.create(dtos);
//
// verify(certificateService, times(1)).create(eq(dtos));
// }
// }
