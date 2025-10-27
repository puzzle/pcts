import { EmploymentState } from '../../../shared/enum/employment-state.enum';

export interface MemberDto {
  firstName: string;
  lastName: string;
  birthDate: Date;
  abbreviation: string;
  employmentState: EmploymentState;
  organisationUnitId: number;
  dateOfHire: Date;
}
