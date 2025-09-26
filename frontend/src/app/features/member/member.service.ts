import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { OrganisationUnit } from './dto/organisation-unit.model';
import { EmploymentState, Member } from './dto/member.model';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
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
      abbreviation: 'JM',
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
