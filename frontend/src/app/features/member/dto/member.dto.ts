import { EmploymentState } from '../../../shared/enum/employment-state.enum';

export interface MemberDto {
  firstName: string;
  lastName: string;
  birthDate: Date;
  abbreviation: string | null;
  employmentState: EmploymentState;
  organisationUnitId: number | undefined;
  dateOfHire: Date | null;
}
