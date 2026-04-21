import { LeadershipExperienceKind } from './leadership-experience-kind.enum';

export interface LeadershipExperienceTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  experienceKind: LeadershipExperienceKind;
}
