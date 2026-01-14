package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static ch.puzzle.pcts.Constants.MEMBER;
import static ch.puzzle.pcts.util.TestData.*;
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

    @DisplayName("Should return LeadershipExperienceDto")
    @Test
    void shouldReturnLeadershipExperienceDto() {
        when(memberMapper.toDto(MEMBER_1)).thenReturn(MEMBER_1_DTO);
        when(leadershipExperienceTypeMapper.toDto(LEADERSHIP_TYPE_1)).thenReturn(LEADERSHIP_TYPE_1_DTO);

        LeadershipExperienceDto resultDto = leadershipExperienceMapper.toDto(LEADERSHIP_CERT_1);

        assertNotNull(resultDto);
        assertEquals(LEADERSHIP_CERT_1_ID, resultDto.id());
        assertEquals(LEADERSHIP_CERT_1.getComment(), resultDto.comment());
        assertEquals(MEMBER_1_DTO, resultDto.member());
        assertEquals(LEADERSHIP_TYPE_1_DTO, resultDto.experience());

        verify(memberMapper).toDto(MEMBER_1);
        verify(leadershipExperienceTypeMapper).toDto(LEADERSHIP_TYPE_1);
    }

    @DisplayName("Should return Certificate model from InputDto")
    @Test
    void shouldReturnCertificate() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(leadershipExperienceTypeBusinessService.getById(LEADERSHIP_TYPE_1_ID)).thenReturn(LEADERSHIP_TYPE_1);

        Certificate resultCertificate = leadershipExperienceMapper.fromDto(LEADERSHIP_CERT_1_INPUT);

        assertNotNull(resultCertificate);
        assertEquals(LEADERSHIP_CERT_1_INPUT.comment(), resultCertificate.getComment());
        assertEquals(MEMBER_1, resultCertificate.getMember());
        assertEquals(LEADERSHIP_TYPE_1, resultCertificate.getCertificateType());

        assertNull(resultCertificate.getCompletedAt());
        assertNull(resultCertificate.getValidUntil());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(leadershipExperienceTypeBusinessService).getById(LEADERSHIP_TYPE_1_ID);
    }

    @DisplayName("Should return a list of LeadershipExperienceDto")
    @Test
    void shouldGetListOfLeadershipExperienceDto() {
        List<Certificate> certificateList = List.of(LEADERSHIP_CERT_1, LEADERSHIP_CERT_2);

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(leadershipExperienceTypeMapper.toDto(any(CertificateType.class)))
                .thenReturn(mock(LeadershipExperienceTypeDto.class));

        List<LeadershipExperienceDto> dtoList = leadershipExperienceMapper.toDto(certificateList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(LEADERSHIP_CERT_1.getId(), dtoList.get(0).id());
        assertEquals(LEADERSHIP_CERT_2.getId(), dtoList.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(leadershipExperienceTypeMapper, times(2)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should return a list of Certificate models")
    @Test
    void shouldGetListOfCertificate() {
        List<LeadershipExperienceInputDto> inputList = List.of(LEADERSHIP_CERT_1_INPUT, LEADERSHIP_CERT_2_INPUT);

        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(memberBusinessService.getById(MEMBER_2_ID)).thenReturn(MEMBER_2);
        when(leadershipExperienceTypeBusinessService.getById(LEADERSHIP_TYPE_1_ID)).thenReturn(LEADERSHIP_TYPE_1);
        when(leadershipExperienceTypeBusinessService.getById(LEADERSHIP_TYPE_2_ID)).thenReturn(LEADERSHIP_TYPE_2);

        List<Certificate> resultList = leadershipExperienceMapper.fromDto(inputList);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(LEADERSHIP_CERT_1.getComment(), resultList.get(0).getComment());
        assertEquals(LEADERSHIP_CERT_2.getComment(), resultList.get(1).getComment());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(memberBusinessService).getById(MEMBER_2_ID);
        verify(leadershipExperienceTypeBusinessService).getById(LEADERSHIP_TYPE_1_ID);
        verify(leadershipExperienceTypeBusinessService).getById(LEADERSHIP_TYPE_2_ID);
    }

    @DisplayName("Should throw exception when member is not found")
    @Test
    void shouldThrowExceptionWhenMemberIsNotFound() {
        LeadershipExperienceInputDto inputDto = new LeadershipExperienceInputDto(INVALID_ID,
                                                                                 LEADERSHIP_TYPE_1_ID,
                                                                                 "This is a comment.");

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
        LeadershipExperienceInputDto inputDto = new LeadershipExperienceInputDto(MEMBER_1_ID,
                                                                                 INVALID_ID,
                                                                                 "This is a comment.");

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