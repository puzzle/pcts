import { MemberModel } from '../../features/member/member.model';
import { EmploymentState } from '../enum/employment-state.enum';
import { OrganisationUnitModel } from '../../features/organisation-unit/organisation-unit.model';

export const organisationUnit1: OrganisationUnitModel = {
  id: 1,
  name: '/mem'
};

export const organisationUnit2: OrganisationUnitModel = {
  id: 2,
  name: '/sys'
};

export const organisationUnit3: OrganisationUnitModel = {
  id: 3,
  name: '/bbt'
};

export const organisationUnit4: OrganisationUnitModel = {
  id: 4,
  name: '/rookie'
};

export const member1: MemberModel = {
  id: 1,
  name: 'Ja',
  lastName: 'Morant',
  birthday: new Date(2000, 12, 1),
  abbreviation: 'JM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date(2018, 12, 1),
  organisationUnit: organisationUnit1
};

export const member2: MemberModel = {
  id: 2,
  name: 'Jaren',
  lastName: 'Jackson Jr',
  birthday: new Date(2000, 12, 1),
  abbreviation: 'JJJ',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date(2018, 12, 1),
  organisationUnit: organisationUnit2
};

export const member3: MemberModel = {
  id: 3,
  name: 'Bane',
  lastName: 'Desmond',
  birthday: new Date(2000, 12, 1),
  abbreviation: 'BD',
  employmentState: EmploymentState.EX_MEMBER,
  dateOfHire: new Date(2018, 12, 1),
  organisationUnit: organisationUnit3
};

export const member4: MemberModel = {
  id: 4,
  name: 'Jaylan',
  lastName: 'Williams',
  birthday: new Date(2000, 12, 1),
  abbreviation: 'JW',
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: new Date(2018, 12, 1),
  organisationUnit: organisationUnit4
};
