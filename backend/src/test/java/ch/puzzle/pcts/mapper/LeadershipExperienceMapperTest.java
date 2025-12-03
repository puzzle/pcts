package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static ch.puzzle.pcts.Constants.MEMBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
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
class LeadershipExperienceMapperTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long LEADERSHIP_TYPE_ID = 10L;
    private static final Long CERT_ID = 100L;
    private static final String COMMENT = "Leadership Comment";

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private MemberBusinessService memberBusinessService;
    @Mock
    private LeadershipExperienceTypeMapper leadershipExperienceTypeMapper;
    @Mock
    private LeadershipExperienceTypeBusinessService leadershipExperienceTypeBusinessService;

    private LeadershipExperienceMapper leadershipExperienceMapper;

    @BeforeEach
    void setUp() {
        leadershipExperienceMapper = new LeadershipExperienceMapper(leadershipExperienceTypeMapper,
                                                                    memberMapper,
                                                                    leadershipExperienceTypeBusinessService,
                                                                    memberBusinessService);
    }

    private Member createTestMember() {
        Member member = new Member();
        member.setId(MEMBER_ID);
        return member;
    }

    private CertificateType createTestCertType() {
        CertificateType certType = new CertificateType();
        certType.setId(LEADERSHIP_TYPE_ID);
        return certType;
    }

    private Certificate createTestCertificate(Member member, CertificateType certType) {
        return Certificate.Builder
                .builder()
                .withId(CERT_ID)
                .withMember(member)
                .withCertificateType(certType)
                .withComment(COMMENT)
                .build();
    }

    private LeadershipExperienceInputDto createTestInputDto() {
        return new LeadershipExperienceInputDto(MEMBER_ID, LEADERSHIP_TYPE_ID, COMMENT);
    }

    @DisplayName("Should return LeadershipExperienceDto")
    @Test
    void shouldReturnLeadershipExperienceDto() {
        Member member = createTestMember();
        CertificateType certType = createTestCertType();
        Certificate certificate = createTestCertificate(member, certType);

        MemberDto expectedMemberDto = mock(MemberDto.class);
        LeadershipExperienceTypeDto expectedTypeDto = mock(LeadershipExperienceTypeDto.class);

        when(memberMapper.toDto(member)).thenReturn(expectedMemberDto);
        when(leadershipExperienceTypeMapper.toDto(certType)).thenReturn(expectedTypeDto);

        LeadershipExperienceDto resultDto = leadershipExperienceMapper.toDto(certificate);

        assertNotNull(resultDto);
        assertEquals(CERT_ID, resultDto.id());
        assertEquals(COMMENT, resultDto.comment());
        assertEquals(expectedMemberDto, resultDto.member());
        assertEquals(expectedTypeDto, resultDto.experience());

        verify(memberMapper).toDto(member);
        verify(leadershipExperienceTypeMapper).toDto(certType);
    }

    @DisplayName("Should return Certificate model from InputDto")
    @Test
    void shouldReturnCertificate() {
        LeadershipExperienceInputDto inputDto = createTestInputDto();
        Member expectedMember = createTestMember();
        CertificateType expectedCertType = createTestCertType();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(expectedMember);
        when(leadershipExperienceTypeBusinessService.getById(LEADERSHIP_TYPE_ID)).thenReturn(expectedCertType);

        Certificate resultCertificate = leadershipExperienceMapper.fromDto(inputDto);

        assertNotNull(resultCertificate);
        assertEquals(inputDto.comment(), resultCertificate.getComment());
        assertEquals(expectedMember, resultCertificate.getMember());
        assertEquals(expectedCertType, resultCertificate.getCertificateType());

        assertNull(resultCertificate.getCompletedAt());
        assertNull(resultCertificate.getValidUntil());

        verify(memberBusinessService).getById(MEMBER_ID);
        verify(leadershipExperienceTypeBusinessService).getById(LEADERSHIP_TYPE_ID);
    }

    @DisplayName("Should return a list of LeadershipExperienceDto")
    @Test
    void shouldGetListOfLeadershipExperienceDto() {
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
        when(leadershipExperienceTypeMapper.toDto(any(CertificateType.class)))
                .thenReturn(mock(LeadershipExperienceTypeDto.class));

        List<LeadershipExperienceDto> dtoList = leadershipExperienceMapper.toDto(certificateList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1L, dtoList.get(0).id());
        assertEquals(2L, dtoList.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(leadershipExperienceTypeMapper, times(2)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should return a list of Certificate models")
    @Test
    void shouldGetListOfCertificate() {
        LeadershipExperienceInputDto input1 = new LeadershipExperienceInputDto(1L, 10L, "c1");
        LeadershipExperienceInputDto input2 = new LeadershipExperienceInputDto(2L, 20L, "c2");
        List<LeadershipExperienceInputDto> inputList = List.of(input1, input2);

        when(memberBusinessService.getById(1L)).thenReturn(new Member());
        when(memberBusinessService.getById(2L)).thenReturn(new Member());
        when(leadershipExperienceTypeBusinessService.getById(10L)).thenReturn(new CertificateType());
        when(leadershipExperienceTypeBusinessService.getById(20L)).thenReturn(new CertificateType());

        List<Certificate> resultList = leadershipExperienceMapper.fromDto(inputList);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("c1", resultList.get(0).getComment());
        assertEquals("c2", resultList.get(1).getComment());

        verify(memberBusinessService).getById(1L);
        verify(memberBusinessService).getById(2L);
        verify(leadershipExperienceTypeBusinessService).getById(10L);
        verify(leadershipExperienceTypeBusinessService).getById(20L);
    }

    @DisplayName("Should throw exception when member is not found")
    @Test
    void shouldThrowExceptionWhenMemberIsNotFound() {
        Long nonExistentMemberId = 999L;
        LeadershipExperienceInputDto inputDto = new LeadershipExperienceInputDto(nonExistentMemberId,
                                                                                 LEADERSHIP_TYPE_ID,
                                                                                 "Comment");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id", FieldKey.IS, "" + inputDto.memberId());

        when(memberBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> leadershipExperienceMapper.fromDto(inputDto));
    }

    @DisplayName("Should throw exception when LeadershipExperienceType is not found")
    @Test
    void shouldThrowExceptionWhenLeadershipExperienceTypeIsNotFound() {
        Long nonExistentTypeId = 999L;
        LeadershipExperienceInputDto inputDto = new LeadershipExperienceInputDto(MEMBER_ID,
                                                                                 nonExistentTypeId,
                                                                                 "Comment");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY,
                    LEADERSHIP_EXPERIENCE_TYPE,
                    FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(inputDto.leadershipExperienceTypeId()));

        when(leadershipExperienceTypeBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> leadershipExperienceMapper.fromDto(inputDto));
    }
}