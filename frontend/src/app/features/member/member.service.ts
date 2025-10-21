import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MemberDto } from './dto/member.dto';

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
    return this.httpClient.get<MemberModel>(`${this.API_URL}/${id}`);
  }

  addMember(member: MemberDto): Observable<MemberModel> {
    return this.httpClient.post<MemberModel>(this.API_URL, member);
  }

  updateMember(id: number, member: MemberDto): Observable<MemberModel> {
    return this.httpClient.put<MemberModel>(`${this.API_URL}/${id}`, member);
  }

  toDto(model: MemberModel): MemberDto {
    return {
      name: model.name,
      lastName: model.lastName,
      birthDate: model.birthDate,
      abbreviation: model.abbreviation,
      employmentState: model.employmentState,
      organisationUnitId: model.organisationUnit?.id,
      dateOfHire: model.dateOfHire
    };
  }
}
