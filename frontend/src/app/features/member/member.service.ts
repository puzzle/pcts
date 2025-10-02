import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { EmploymentState } from '../../shared/enum/employment-state.enum';
import {OrganisationUnitModel} from '../organisation-unit/organisation-unit.model';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';


  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL);
  }

  getAllOrganisationUnits(): Observable<OrganisationUnitModel[]> {
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

  getMemberById(id: number): Observable<MemberModel> {
    return of({
      id: 1,
      name: 'Ja',
      lastName: 'Morant',
      birthDate: new Date(),
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
}
