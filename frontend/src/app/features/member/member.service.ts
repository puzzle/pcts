import { Injectable } from '@angular/core';
import { MemberDto } from './member.dto';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  getAllMembers(): Observable<MemberDto[]> {
    return of([
      {
        id: 1,
        name: 'Ja',
        lastName: 'Morant',
        birthday: new Date(),
        abbreviation: 'JM',
        employmentState: 'Member',
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
        employmentState: 'Member',
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
        employmentState: 'Ex Member',
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
        employmentState: 'Bewerber',
        dateOfHire: new Date(),
        organisationUnit: {
          id: 1,
          name: '/rookie'
        }
      }
    ]);
  }
}
