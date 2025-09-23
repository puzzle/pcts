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
        last_name: 'Morant',
        birthday: new Date(),
        abbreviation: 'JM',
        employment_state: EmploymentState.MEMBER,
        date_of_hire: new Date(),
        is_admin: false,
        organisation_unit: '/mem'
      },
      {
        id: 2,
        name: 'jaren',
        last_name: 'jackson jr',
        birthday: new Date(),
        abbreviation: 'JJJ',
        employment_state: EmploymentState.MEMBER,
        date_of_hire: new Date(),
        is_admin: false,
        organisation_unit: '/mem'
      },
      {
        id: 2,
        name: 'jaylen',
        last_name: 'Williams',
        birthday: new Date(),
        abbreviation: 'JW',
        employment_state: EmploymentState.BEWERBER,
        date_of_hire: new Date(),
        is_admin: false,
        organisation_unit: '/mem'
      },
      {
        id: 2,
        name: 'Bane',
        last_name: 'Desmond',
        birthday: new Date(),
        abbreviation: 'JW',
        employment_state: EmploymentState.EX_MEMBER,
        date_of_hire: new Date(),
        is_admin: false,
        organisation_unit: '/mem'
      }
    ]);
  }
}
