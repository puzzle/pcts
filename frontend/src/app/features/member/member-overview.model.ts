import { EmploymentState } from '../../shared/enum/employment-state.enum';

export interface MemberOverviewModel {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: Date;
  abbreviation: string | null;
  employmentState: EmploymentState;
  organisationUnitName: string | null;
  dateOfHire: Date | null;
}
