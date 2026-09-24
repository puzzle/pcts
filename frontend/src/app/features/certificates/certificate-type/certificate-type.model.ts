import { TagModel } from './tag.model';

export interface CertificateTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  tags: TagModel[];
  effort: number;
  examDuration: number;
  link: string;
  examType: ExamType;
  publisher: string;

}

export enum ExamType {
  MULTIPLE_CHOICE,
  THEORETICAL,
  PRACTICAL,
  THEORETICAL_AND_PRACTICAL,
  CASE_STUDY,
  WRITTEN_ASSIGNMENT,
  NONE
}
