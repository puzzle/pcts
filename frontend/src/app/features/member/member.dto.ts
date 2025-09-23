import { OrganisationUnitDto } from './organisation-unit.dto';

export interface MemberDto {
  id: number;
  name: string;
  lastName: string;
  birthday: Date;
  abbreviation: string;
  employmentState: string;
  organisationUnit: OrganisationUnitDto;
  dateOfHire: Date;
}
