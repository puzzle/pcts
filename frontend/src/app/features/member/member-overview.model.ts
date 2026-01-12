import { MemberModel } from './member.model';
import { CvOverviewModel } from './cv.model';

export interface MemberOverviewModel {
  member: MemberModel;
  cv: CvOverviewModel;
}
