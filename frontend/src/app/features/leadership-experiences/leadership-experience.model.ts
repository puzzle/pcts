import { MemberModel } from '../member/member.model';
import { LeadershipExperienceTypeModel } from './leadership-experiences-type/leadership-experience-type.model';

export interface LeadershipExperienceModel {
  id: number;
  member: MemberModel;
  experience: LeadershipExperienceTypeModel;
  comment: string | null;
}
