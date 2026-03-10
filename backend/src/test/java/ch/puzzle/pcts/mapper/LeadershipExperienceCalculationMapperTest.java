package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceCalculationMapperTest {

    private static final Long EXPERIENCE_ID = 10L;

    @Mock
    private LeadershipExperienceCalculationInputDto dto;

    @Mock
    private LeadershipExperienceBusinessService leadershipExperienceBusinessService;

    @Mock
    private LeadershipExperienceMapper leadershipExperienceMapper;

    @InjectMocks
    private LeadershipExperienceCalculationMapper mapper;

    private LeadershipExperience createLeadershipExperience() {
        LeadershipExperience experience = new LeadershipExperience();
        experience.setId(1L);
        experience.setMember(new Member());
        LeadershipExperienceType type = new LeadershipExperienceType();
        experience.setLeadershipExperienceType(type);
        return experience;
    }

    private LeadershipExperienceCalculation createLeadershipExperienceCalculation(LeadershipExperience leadershipExperience) {
        return new LeadershipExperienceCalculation(5L, null, leadershipExperience);
    }

    private LeadershipExperienceDto mockLeadershipExperienceDto() {
        return mock(LeadershipExperienceDto.class);
    }

    @DisplayName("Should map LeadershipExperienceCalculation to LeadershipExperienceCalculationDto")
    @Test
    void shouldMapToDto() {
        LeadershipExperience experience = createLeadershipExperience();
        LeadershipExperienceCalculation model = createLeadershipExperienceCalculation(experience);

        LeadershipExperienceDto mockedDto = mockLeadershipExperienceDto();
        when(leadershipExperienceMapper.toDto(experience)).thenReturn(mockedDto);

        LeadershipExperienceCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals(mockedDto, result.experience());

        verify(leadershipExperienceMapper).toDto(experience);
    }

    @DisplayName("Should map List<LeadershipExperienceCalculation> to List<LeadershipExperienceCalculationDto>")
    @Test
    void shouldMapListToDto() {
        LeadershipExperience leadershipExperience = createLeadershipExperience();
        LeadershipExperienceCalculation leadershipCalc = createLeadershipExperienceCalculation(leadershipExperience);

        LeadershipExperienceDto mockedDto = mockLeadershipExperienceDto();
        when(leadershipExperienceMapper.toDto(leadershipExperience)).thenReturn(mockedDto);

        List<LeadershipExperienceCalculationDto> result = mapper.toDto(List.of(leadershipCalc));

        assertEquals(1, result.size());
        assertEquals(mockedDto, result.getFirst().experience());

        verify(leadershipExperienceMapper).toDto(leadershipExperience);
    }

    @DisplayName("Should map LeadershipExperienceCalculationInputDto to LeadershipExperienceCalculation")
    @Test
    void shouldMapFromDto() {
        LeadershipExperience experience = createLeadershipExperience();

        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID)).thenReturn(experience);
        when(dto.id()).thenReturn(null);
        when(dto.leadershipExperienceId()).thenReturn(EXPERIENCE_ID);

        LeadershipExperienceCalculation result = mapper.fromDto(dto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(experience, result.getLeadershipExperience());

        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should map List<LeadershipExperienceCalculationInputDto> to List<LeadershipExperienceCalculation>")
    @Test
    void shouldMapListFromDto() {
        when(dto.id()).thenReturn(null);
        when(dto.leadershipExperienceId()).thenReturn(EXPERIENCE_ID);
        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID))
                .thenReturn(createLeadershipExperience());

        List<LeadershipExperienceCalculation> result = mapper.fromDto(List.of(dto));

        assertEquals(1, result.size());
        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }
}