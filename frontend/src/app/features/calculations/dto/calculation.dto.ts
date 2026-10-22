import { CalculationState } from '../calculation-state.enum';
import { CertificateCalculationModel } from '../certificate-calculation/certificate-calculation.model';
import {
  LeadershipExperienceCalculationModel
} from '../leadership-experience-calculation/leadership-experience-calculation.model';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.model';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';

export interface CalculationInputDto {
  memberId: number;
  state: CalculationState;
  roleId: number;
  certificateCalculations: CertificateCalculationModel[];
  leadershipExperienceCalculations: LeadershipExperienceCalculationModel[];
  degreeCalculations: DegreeCalculationModel[];
  experienceCalculations: ExperienceCalculationModel[];
}
