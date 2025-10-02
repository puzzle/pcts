import { Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { Observable, of } from 'rxjs';
import { OrganisationUnit } from './dto/organisation-unit.model';
import { EmploymentState } from '../../shared/enum/employment-state.enum';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  getAllMembers(): Observable<MemberModel[]> {
    return of([
      {
        id: 1,
        name: 'Ja',
        lastName: 'Morant',
        birthday: new Date(),
        abbreviation: 'JM',
        employmentState: EmploymentState.MEMBER,
        dateOfHire: new Date(),
        organisationUnit: {
          id: 1,
          name: '/mem'
        }
      },
      {
        id: 2,
        name: 'Jaren',
        lastName: 'Jackson Jr',
        birthday: new Date(),
        abbreviation: 'JJJ',
        employmentState: EmploymentState.MEMBER,
        dateOfHire: new Date(),
        organisationUnit: {
          id: 1,
          name: '/mem'
        }
      },
      {
        id: 3,
        name: 'Bane',
        lastName: 'Desmond',
        birthday: new Date(),
        abbreviation: 'BD',
        employmentState: EmploymentState.EX_MEMBER,
        dateOfHire: new Date(),
        organisationUnit: {
          id: 1,
          name: '/mem'
        }
      },
      {
        id: 4,
        name: 'Jaylan',
        lastName: 'Williams',
        birthday: new Date(),
        abbreviation: 'JW',
        employmentState: EmploymentState.APPLICANT,
        dateOfHire: new Date(),
        organisationUnit: {
          id: 1,
          name: '/rookie'
        }
      }
    ]);
  getAllOrganisationUnits(): Observable<OrganisationUnit[]> {
    return of([
      {
        id: 1,
        name: '/mobility'
      },
      {
        id: 2,
        name: '/bbt'
      },
      {
        id: 3,
        name: '/sys'
      },
      {
        id: 4,
        name: '/mem'
      }
    ]);
  }

  getMemberById(id: number): Observable<Member> {
    return of({
      id: 1,
      name: 'Ja',
      last_name: 'Morant',
      birthday: new Date(),
      nickname: 'JM',
      employment_state: EmploymentState.MEMBER,
      date_of_hire: new Date(),
      is_admin: false,
      organisation_unit: {
        id: 4,
        name: '/mem'
      }
    });
  }

  addMember(member: Member): Observable<Member> {
    console.log(member);
    return of({
      ...member
    });
  }
}
