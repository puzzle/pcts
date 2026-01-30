import { MemberModel } from '../member/member.model';
import { RoleModel } from '../roles/role.model';
import { CalculationState } from './calculation-state.enum';
import { CertificateCalculationModel } from './certificate-calculation/certificate-calculation.model';
import {
  LeadershipExperienceCalculationModel
} from './leadership-experience-calculation/leadership-experience-calculation.model';
import { DegreeCalculationModel } from './degree-calculation/degree-calculation.model';
import { ExperienceCalculationModel } from './experience-calculation/experience-calculation.model';

export interface CalculationModel {
  id: number;
  member: MemberModel;
  role: RoleModel;
  state: CalculationState;
  publicationDate: Date | null;
  publicizedBy: string | null;
  points: number;
  certificateCalculations: CertificateCalculationModel[];
  leadershipExperienceCalculations: LeadershipExperienceCalculationModel[];
  degreeCalculations: DegreeCalculationModel[];
  experienceCalculations: ExperienceCalculationModel[];
}
