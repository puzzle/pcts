import { CalculationState } from '../calculation-state.enum';
import { CertificateCalculationInputDto } from '../certificate-calculation/dto/certificate-calculation.dto';
import {
  LeadershipExperienceCalculationInputDto
} from '../leadership-experience-calculation/dto/leadership-experience-calculation.dto';
import { DegreeCalculationInputDto } from '../degree-calculation/dto/degree-calculation.dto';
import { ExperienceCalculationInputDto } from '../experience-calculation/dto/experience-calculation.dto';

export interface CalculationInputDto {
  memberId: number;
  state: CalculationState;
  roleId: number;
  certificateCalculations: CertificateCalculationInputDto[];
  leadershipExperienceCalculations: LeadershipExperienceCalculationInputDto[];
  degreeCalculations: DegreeCalculationInputDto[];
  experienceCalculations: ExperienceCalculationInputDto[];
}
