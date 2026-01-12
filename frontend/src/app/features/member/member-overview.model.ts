import { MemberModel } from './member.model';
import { CvOverviewModel } from './detail-view/cv/cv.model';

export interface MemberOverviewModel {
  member: MemberModel;
  cv: CvOverviewModel;
}
