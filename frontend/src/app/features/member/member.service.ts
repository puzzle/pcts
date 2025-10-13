import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { EmploymentState } from '../../shared/enum/employment-state.enum';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';


  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL);
  }

  getMemberById(id: number): Observable<MemberModel> {
    return of({
      id: 1,
      name: 'Ja',
      lastName: 'Morant',
      birthDate: new Date(2000, 0, 1),
      abbreviation: 'JM',
      employmentState: EmploymentState.MEMBER,
      dateOfHire: new Date(2025, 0, 1),
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
