import { MemberModel } from '../member/member.model';
import { ExperienceTypeModel } from '../experiences/experience-type/experience-type.model';

export interface LeadershipExperiencesModel {
  id: number;
  member: MemberModel;
  experienceType: ExperienceTypeModel;
  comment: string;
}
