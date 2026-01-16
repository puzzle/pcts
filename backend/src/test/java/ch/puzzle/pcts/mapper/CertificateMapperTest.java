package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;
import static ch.puzzle.pcts.Constants.MEMBER;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
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

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private MemberBusinessService memberBusinessService;
    @Mock
    private CertificateTypeMapper certificateTypeMapper;
    @Mock
    private CertificateTypeBusinessService certificateTypeBusinessService;

    private CertificateMapper certificateMapper;

    @BeforeEach
    void setUp() {
        certificateMapper = new CertificateMapper(memberMapper,
                                                  certificateTypeMapper,
                                                  memberBusinessService,
                                                  certificateTypeBusinessService);
    }

    @DisplayName("Should return certificate dto")
    @Test
    void shouldReturnCertificateDto() {
        MemberDto expectedMemberDto = mock(MemberDto.class);
        CertificateTypeDto expectedCertTypeDto = mock(CertificateTypeDto.class);

        when(memberMapper.toDto(MEMBER_1)).thenReturn(expectedMemberDto);
        when(certificateTypeMapper.toDto(CERT_TYPE_1)).thenReturn(expectedCertTypeDto);

        CertificateDto resultDto = certificateMapper.toDto(CERTIFICATE_1);

        assertNotNull(resultDto);
        assertEquals(CERTIFICATE_1_ID, resultDto.id());
        assertEquals(CERTIFICATE_1.getCompletedAt(), resultDto.completedAt());
        assertEquals(CERTIFICATE_1.getValidUntil(), resultDto.validUntil());
        assertEquals(CERTIFICATE_1.getComment(), resultDto.comment());
        assertEquals(expectedMemberDto, resultDto.member());

        assertEquals(expectedCertTypeDto, resultDto.certificate());

        verify(memberMapper).toDto(MEMBER_1);
        verify(certificateTypeMapper).toDto(CERT_TYPE_1);
    }

    @DisplayName("Should return Certificate")
    @Test
    void shouldReturnCertificate() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(certificateTypeBusinessService.getById(CERT_TYPE_1_ID)).thenReturn(CERT_TYPE_1);

        Certificate resultCertificate = certificateMapper.fromDto(CERTIFICATE_1_INPUT);

        assertNotNull(resultCertificate);
        assertEquals(CERTIFICATE_1_INPUT.completedAt(), resultCertificate.getCompletedAt());
        assertEquals(CERTIFICATE_1_INPUT.validUntil(), resultCertificate.getValidUntil());
        assertEquals(CERTIFICATE_1_INPUT.comment(), resultCertificate.getComment());
        assertEquals(MEMBER_1, resultCertificate.getMember());
        assertEquals(CERT_TYPE_1, resultCertificate.getCertificateType());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(certificateTypeBusinessService).getById(CERT_TYPE_1_ID);
    }

    @DisplayName("Should return a list of Certificate dto")
    @Test
    void shouldGetListOfCertificateDto() {
        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(certificateTypeMapper.toDto(any(CertificateType.class))).thenReturn(mock(CertificateTypeDto.class));

        List<CertificateDto> dtoList = certificateMapper.toDto(List.of(CERTIFICATE_1, CERTIFICATE_2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(CERTIFICATE_1_ID, dtoList.get(0).id());
        assertEquals(CERTIFICATE_2_ID, dtoList.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(certificateTypeMapper, times(2)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should return a list of Certificate")
    @Test
    void shouldGetListOfCertificate() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(memberBusinessService.getById(MEMBER_2_ID)).thenReturn(MEMBER_2);
        when(certificateTypeBusinessService.getById(CERT_TYPE_1_ID)).thenReturn(CERT_TYPE_1);
        when(certificateTypeBusinessService.getById(CERT_TYPE_2_ID)).thenReturn(CERT_TYPE_2);

        List<Certificate> resultList = certificateMapper.fromDto(List.of(CERTIFICATE_1_INPUT, CERTIFICATE_2_INPUT));

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(CERTIFICATE_1_INPUT.comment(), resultList.get(0).getComment());
        assertEquals(CERTIFICATE_2_INPUT.comment(), resultList.get(1).getComment());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(memberBusinessService).getById(MEMBER_2_ID);
        verify(certificateTypeBusinessService).getById(CERT_TYPE_1_ID);
        verify(certificateTypeBusinessService).getById(CERT_TYPE_2_ID);
    }

    @DisplayName("Should throw exception when member is not found")
    @Test
    void shouldThrowExceptionWhenMemberIsNotFound() {
        CertificateInputDto inputDto = new CertificateInputDto(INVALID_ID,
                                                               CERT_TYPE_1_ID,
                                                               DATE_NOW,
                                                               DATE_NOW,
                                                               "Comment");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id", FieldKey.IS, "" + inputDto.memberId());

        // mock the behavior
        when(certificateMapper.memberBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> certificateMapper.fromDto(inputDto));
    }

    @DisplayName("Should throw exception when Certificate-Type is not found")
    @Test
    void shouldThrowExceptionWhenCertificateTypeIsNotFound() {
        CertificateInputDto inputDto = new CertificateInputDto(MEMBER_1_ID, INVALID_ID, DATE_NOW, DATE_NOW, "Comment");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY,
                    CERTIFICATE_TYPE,
                    FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(inputDto.certificateTypeId()));

        // mock the behavior
        when(certificateMapper.certificateTypeBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> certificateMapper.fromDto(inputDto));
    }
}