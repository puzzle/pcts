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

export interface DegreeInputDto {
  memberId: number;
  name: string;
  institution: string | null;
  completed: boolean;
  typeId: number;
  startDate: Date;
  endDate: Date | null;
  comment: string | null;

}
