import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { RoleModel } from '../../roles/RoleModel';

export interface MemberDto {
  firstName: string;
  lastName: string;
  birthDate: string | null;
  abbreviation: string | null;
  employmentState: EmploymentState;
  organisationUnitId: number | undefined;
  roles: RoleModel[] | undefined;
  dateOfHire: string | null;
}
