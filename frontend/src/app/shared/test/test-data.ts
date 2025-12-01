import { MemberModel } from '../../features/member/member.model';
import { EmploymentState } from '../enum/employment-state.enum';
import { OrganisationUnitModel } from '../../features/organisation-unit/organisation-unit.model';
import { MemberDto } from '../../features/member/dto/member.dto';
import { TagModel } from '../../features/certificates/certificate-type/tag.model';
import { CertificateTypeModel } from '../../features/certificates/certificate-type/certificate-type.model';
import { CertificateKind } from '../../features/certificates/certificate-type/certificate-kind.enum';
import { CertificateModel } from '../../features/certificates/certificate.model';
import { CertificateDto } from '../../features/certificates/dto/certificate.dto';

export const url = '/api/v1/data';

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
  firstName: 'Lena',
  lastName: 'Müller',
  birthDate: new Date('2000-12-01'),
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit1
};

export const member2: MemberModel = {
  id: 2,
  firstName: 'Sophie',
  lastName: 'Keller',
  birthDate: new Date('2000-12-01'),
  abbreviation: 'SK',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit2
};

export const member3: MemberModel = {
  id: 3,
  firstName: 'Mara',
  lastName: 'Becker',
  birthDate: new Date('2000-12-01'),
  abbreviation: 'BD',
  employmentState: EmploymentState.EX_MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit3
};

export const member4: MemberModel = {
  id: 4,
  firstName: 'John',
  lastName: 'Doe',
  birthDate: new Date('2000-12-01'),
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnit: organisationUnit4
};

export const memberDto1: MemberDto = {
  firstName: 'Lena',
  lastName: 'Müller',
  birthDate: '2000-12-01',
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: '2018-12-01',
  organisationUnitId: 1
};

export const memberDto2: MemberDto = {
  firstName: 'John',
  lastName: 'Doe',
  birthDate: '2000-12-01',
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnitId: 4
};

export const tag1: TagModel = {
  id: 1,
  name: 'GitLab'
};

export const tag2: TagModel = {
  id: 2,
  name: 'AWS'
};

export const tag3: TagModel = {
  id: 3,
  name: 'Ruby'
};

export const tag4: TagModel = {
  id: 4,
  name: 'PHP'
};

export const certificateType1: CertificateTypeModel = {
  id: 1,
  name: 'GitLab & AWS Certificate',
  points: 10,
  comment: null,
  tags: [tag1,
    tag2],
  certificateKind: CertificateKind.CERTIFICATE
};

export const certificateType2: CertificateTypeModel = {
  id: 2,
  name: 'Ruby Certificate',
  points: 15,
  comment: null,
  tags: [tag3],
  certificateKind: CertificateKind.CERTIFICATE
};

export const certificateType3: CertificateTypeModel = {
  id: 2,
  name: 'Advanced Military Function',
  points: 25,
  comment: 'Requires prior experience',
  tags: [],
  certificateKind: CertificateKind.MILITARY_FUNCTION
};

export const certificateType4: CertificateTypeModel = {
  id: 3,
  name: 'Youth Sports Training',
  points: 15,
  comment: null,
  tags: [],
  certificateKind: CertificateKind.YOUTH_AND_SPORT
};

export const certificate1: CertificateModel = {
  id: 1,
  member: member1,
  certificateType: certificateType1,
  completedAt: new Date('2022-03-15'),
  validUntil: new Date('2024-03-15'),
  comment: null
};

export const certificate2: CertificateModel = {
  id: 2,
  member: member2,
  certificateType: certificateType2,
  completedAt: new Date('2021-10-10'),
  validUntil: null,
  comment: 'Completed with distinction'
};

export const certificateDto1: CertificateDto = {
  memberId: 3,
  certificateTypeId: 3,
  completedAt: '2020-06-01',
  validUntil: '2023-01-01',
  comment: null
};

export const certificateDto2: CertificateDto = {
  memberId: 4,
  certificateTypeId: 4,
  completedAt: '2019-09-20',
  validUntil: '2024-09-20',
  comment: 'Leadership excellence'
};
