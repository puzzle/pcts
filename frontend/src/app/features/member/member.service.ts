import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { EmploymentState } from '../../shared/enum/employment-state.enum';
import { MemberModel } from './member.model';

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
  }

  getMemberById(id: number): Observable<MemberModel> {
    return of({
      id: 1,
      name: 'Ja',
      lastName: 'Morant',
      birthday: new Date(),
      abbreviation: 'JM',
      employmentState: EmploymentState.MEMBER,
      dateOfHire: new Date(),
      organisationUnit: {
        id: 4,
        name: '/mem'
      }
    });
  }

  addMember(member: MemberModel): Observable<MemberModel> {
    console.log(member);
    return of({
      ...member
    });
  }

  updateMember(id: number, member: MemberModel): Observable<MemberModel> {
    console.log(id, member);
    return of({
      ...member
    });
  }
}

