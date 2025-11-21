package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CertificateMapperTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long CERT_TYPE_ID = 10L;
    private static final Long CERT_ID = 100L;
    private static final LocalDate COMPLETED_AT = LocalDate.of(2023, 1, 1);
    private static final LocalDate VALID_UNTIL = LocalDate.of(2025, 1, 1);
    private static final String COMMENT = "Test comment";

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private MemberBusinessService memberBusinessService;
    @Mock
    private CertificateTypeMapper certificateTypeMapper;
    @Mock
    private CertificateTypeBusinessService certificateTypeBusinessService;
    @Mock
    private MemberPersistenceService memberPersistenceService;
    @Mock
    private MemberValidationService memberValidationService;

    private CertificateMapper certificateMapper;

    @BeforeEach
    void setUp() {
        certificateMapper = new CertificateMapper(memberMapper,
                                                  certificateTypeMapper,
                                                  memberBusinessService,
                                                  certificateTypeBusinessService);
    }

    private Member createTestMember() {
        Member member = new Member();
        member.setId(MEMBER_ID);
        return member;
    }

    private CertificateType createTestCertType() {
        CertificateType certType = new CertificateType();
        certType.setId(CERT_TYPE_ID);
        return certType;
    }

    private Certificate createTestCertificate(Member member, CertificateType certType) {
        return Certificate.Builder
                .builder()
                .withId(CERT_ID)
                .withMember(member)
                .withCertificateType(certType)
                .withCompletedAt(COMPLETED_AT)
                .withValidUntil(VALID_UNTIL)
                .withComment(COMMENT)
                .build();
    }

    private CertificateInputDto createTestInputDto() {
        return new CertificateInputDto(MEMBER_ID, CERT_TYPE_ID, COMPLETED_AT, VALID_UNTIL, "Comment");
    }

    @DisplayName("Should return certificate dto")
    @Test
    void shouldReturnCertificateDto() {
        Member member = createTestMember();
        CertificateType certType = createTestCertType();
        Certificate certificate = createTestCertificate(member, certType);

        MemberDto expectedMemberDto = mock(MemberDto.class);
        CertificateTypeDto expectedCertTypeDto = mock(CertificateTypeDto.class);

        when(memberMapper.toDto(member)).thenReturn(expectedMemberDto);
        when(certificateTypeMapper.toDto(certType)).thenReturn(expectedCertTypeDto);

        CertificateDto resultDto = certificateMapper.toDto(certificate);

        assertNotNull(resultDto);
        assertEquals(CERT_ID, resultDto.id());
        assertEquals(COMPLETED_AT, resultDto.completedAt());
        assertEquals(VALID_UNTIL, resultDto.validUntil());
        assertEquals(COMMENT, resultDto.comment());
        assertEquals(expectedMemberDto, resultDto.member());

        assertEquals(expectedCertTypeDto, resultDto.certificate());

        verify(memberMapper).toDto(member);
        verify(certificateTypeMapper).toDto(certType);
    }

    @DisplayName("Should return Certificate")
    @Test
    void shouldReturnCertificate() {
        CertificateInputDto inputDto = createTestInputDto();
        Member expectedMember = createTestMember();
        CertificateType expectedCertType = createTestCertType();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(expectedMember);
        when(certificateTypeBusinessService.getById(CERT_TYPE_ID)).thenReturn(expectedCertType);

        Certificate resultCertificate = certificateMapper.fromDto(inputDto);

        assertNotNull(resultCertificate);
        assertEquals(inputDto.completedAt(), resultCertificate.getCompletedAt());
        assertEquals(inputDto.validUntil(), resultCertificate.getValidUntil());
        assertEquals(inputDto.comment(), resultCertificate.getComment());
        assertEquals(expectedMember, resultCertificate.getMember());
        assertEquals(expectedCertType, resultCertificate.getCertificateType());

        verify(memberBusinessService).getById(MEMBER_ID);
        verify(certificateTypeBusinessService).getById(CERT_TYPE_ID);
    }

    @DisplayName("Should return a list of Certificate dto")
    @Test
    void shouldGetListOfCertificateDto() {
        Certificate cert1 = Certificate.Builder
                .builder()
                .withId(1L)
                .withMember(new Member())
                .withCertificateType(new CertificateType())
                .build();
        Certificate cert2 = Certificate.Builder
                .builder()
                .withId(2L)
                .withMember(new Member())
                .withCertificateType(new CertificateType())
                .build();
        List<Certificate> certificateList = List.of(cert1, cert2);

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(certificateTypeMapper.toDto(any(CertificateType.class))).thenReturn(mock(CertificateTypeDto.class));

        List<CertificateDto> dtoList = certificateMapper.toDto(certificateList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1L, dtoList.get(0).id());
        assertEquals(2L, dtoList.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(certificateTypeMapper, times(2)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should return a list of Certificate")
    @Test
    void shouldGetListOfCertificate() {
        CertificateInputDto input1 = new CertificateInputDto(1L, 10L, LocalDate.now(), null, "c1");
        CertificateInputDto input2 = new CertificateInputDto(2L, 20L, LocalDate.now(), null, "c2");
        List<CertificateInputDto> dtoList = List.of(input1, input2);

        when(memberBusinessService.getById(1L)).thenReturn(new Member());
        when(memberBusinessService.getById(2L)).thenReturn(new Member());
        when(certificateTypeBusinessService.getById(10L)).thenReturn(new CertificateType());
        when(certificateTypeBusinessService.getById(20L)).thenReturn(new CertificateType());

        List<Certificate> resultList = certificateMapper.fromDto(dtoList);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("c1", resultList.get(0).getComment());
        assertEquals("c2", resultList.get(1).getComment());

        verify(memberBusinessService).getById(1L);
        verify(memberBusinessService).getById(2L);
        verify(certificateTypeBusinessService).getById(10L);
        verify(certificateTypeBusinessService).getById(20L);
    }

    @DisplayName("Should throw exception when certificate is not found")
    @Test
    void shouldThrowExceptionWhenCertificateIsNotFound() {
        Long nonExistentMemberId = 999L;
        CertificateInputDto inputDto = new CertificateInputDto(nonExistentMemberId,
                                                               CERT_TYPE_ID,
                                                               COMPLETED_AT,
                                                               VALID_UNTIL,
                                                               "Comment");

        // mock the behavior
        when(certificateMapper.memberBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             ErrorKey.NOT_FOUND,
                                             Map
                                                     .of(FieldKey.ENTITY,
                                                         "member",
                                                         FieldKey.FIELD,
                                                         "id",
                                                         FieldKey.IS,
                                                         "" + inputDto.memberId())));

        assertThrows(PCTSException.class, () -> certificateMapper.fromDto(inputDto));
    }

    @DisplayName("Should throw exception when Certificate-Type is not found")
    @Test
    void shouldThrowExceptionWhenCertificateTypeIsNotFound() {
        Long nonExistentCertificateTypeId = 999L;
        CertificateInputDto inputDto = new CertificateInputDto(MEMBER_ID,
                                                               nonExistentCertificateTypeId,
                                                               COMPLETED_AT,
                                                               VALID_UNTIL,
                                                               "Comment");

        // mock the behavior
        when(certificateMapper.certificateTypeBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             ErrorKey.NOT_FOUND,
                                             Map
                                                     .of(FieldKey.ENTITY,
                                                         "degreeType",
                                                         FieldKey.FIELD,
                                                         "id",
                                                         FieldKey.IS,
                                                         "" + inputDto.certificateTypeId())));

        assertThrows(PCTSException.class, () -> certificateMapper.fromDto(inputDto));
    }
}