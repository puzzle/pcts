import { EmploymentState } from '../../../shared/enum/employment-state.enum';

export interface MemberDto {
  firstName: string;
  lastName: string;
  birthDate: string | null;
  abbreviation: string | null;
  email: string | null;
  employmentState: EmploymentState;
  organisationUnitId: number | undefined;
  dateOfHire: string | null;
}
