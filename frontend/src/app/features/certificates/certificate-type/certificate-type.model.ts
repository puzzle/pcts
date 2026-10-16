export interface CertificateTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  tags: string[];
  effort: number | null;
  examDuration: number | null;
  link: string | null;
  examType: ExamType;
  publisher: string | null;

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
