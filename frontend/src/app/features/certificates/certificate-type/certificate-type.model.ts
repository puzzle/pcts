export interface CertificateTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  tags: string[];
  effort: number;
  examDuration: number;
  link: string;
  examType: ExamType;
  publisher: string;

}

export enum ExamType {
  MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
  THEORETICAL = 'THEORETICAL',
  PRACTICAL = 'PRACTICAL',
  THEORETICAL_AND_PRACTICAL = 'THEORETICAL_AND_PRACTICAL',
  CASE_STUDY = 'CASE_STUDY',
  WRITTEN_ASSIGNMENT = 'WRITTEN_ASSIGNMENT',
  NONE = 'NONE'
}
