import { LeadershipExperienceKind } from '../../../leadership-experiences/leadership-experiences-type/leadership-experience-kind.enum';

export interface LeadershipExperienceTypeOverviewModel {
  name: string;
  leadershipExperienceKind: LeadershipExperienceKind;
}
