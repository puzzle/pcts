import { MemberModel } from '../member/member.model';
import { ExperienceTypeModel } from './experience-type/experience-type.model';

export interface ExperienceModel {
  id: number;
  name: string;
  member: MemberModel;
  experienceType: ExperienceTypeModel;
  comment: string | null;
  employer: string | null;
  percent: number;
  startDate: Date;
  endDate: Date | null;
}
