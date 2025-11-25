import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient } from '@angular/common/http';
import { from, mergeMap, Observable, tap, toArray } from 'rxjs';
import { MemberDto } from './dto/member.dto';
import { DateTime } from 'luxon';
import { removeTimeZone } from '../../shared/utils/DateHandler';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';

  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL)
      .pipe(mergeMap((members) => from(members)), this.formatDate(), toArray());
  }


  getMemberById(id: number): Observable<MemberModel> {
    return this.httpClient.get<MemberModel>(`${this.API_URL}/${id}`)
      .pipe(this.formatDate());
  }

  addMember(member: MemberModel): Observable<MemberModel> {
    return this.httpClient.post<MemberModel>(this.API_URL, this.toDto(member))
      .pipe(this.formatDate());
  }

  updateMember(id: number, member: MemberModel): Observable<MemberModel> {
    return this.httpClient.put<MemberModel>(`${this.API_URL}/${id}`, this.toDto(member))
      .pipe(this.formatDate());
  }

  /*
   *This is a temporary solution to read the Date we receive from backend
   */
  formatDate() {
    return tap((member: MemberModel) => {
      member.birthDate = DateTime.fromISO(member.birthDate.toString());
      if (member.dateOfHire) {
        member.dateOfHire = DateTime.fromISO(member.dateOfHire.toString());
      }
    });
  }

  toDto(model: MemberModel): MemberDto {
    return {
      firstName: model.firstName,
      lastName: model.lastName,
      birthDate: removeTimeZone(model.birthDate.toJSDate()),
      abbreviation: model.abbreviation,
      employmentState: model.employmentState,
      organisationUnitId: model.organisationUnit?.id,
      dateOfHire: model.dateOfHire ? removeTimeZone(model.dateOfHire.toJSDate()) : null
    };
  }
}
