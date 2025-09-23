import { Injectable } from '@angular/core';
import { EmploymentState, Member } from './member.model';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  getAllMembers(): Observable<Member[]> {
    return of([
      {
        id: 1,
        name: 'Ja',
        lastName: 'Morant',
        birthday: new Date(),
        abbreviation: 'JM',
        employmentState: EmploymentState.ACTIVE,
        dateOfHire: new Date(),
        organisationUnit: '/mem'
      },
      {
        id: 2,
        name: 'Jaren',
        lastName: 'Jackson Jr',
        birthday: new Date(),
        abbreviation: 'JJJ',
        employmentState: EmploymentState.ACTIVE,
        dateOfHire: new Date(),
        organisationUnit: '/mem'
      },
      {
        id: 3,
        name: 'Bane',
        lastName: 'Desmond',
        birthday: new Date(),
        abbreviation: 'BD',
        employmentState: EmploymentState.ALUMNI,
        dateOfHire: new Date(),
        organisationUnit: '/mem'
      },
      {
        id: 4,
        name: 'Jaylan',
        lastName: 'Williams',
        birthday: new Date(),
        abbreviation: 'JW',
        employmentState: EmploymentState.APPLICANT,
        dateOfHire: new Date(),
        organisationUnit: '/mem'
      }
    ]);
  }
}
