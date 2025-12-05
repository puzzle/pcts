import { MemberModel } from '../member/member.model';
import { DegreeTypeModel } from './degree-type/degree-type.model';

export interface DegreeModel {
  id: number;
  name: string;
  member: MemberModel;
  type: DegreeTypeModel;
  institution: string | null;
  completed: boolean;
  comment: string | null;
  startDate: Date;
  endDate: Date | null;
}
