import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { MemberDto } from './dto/member.dto';
import { DateTime } from 'luxon';
import { MemberCvOverviewModel } from './member-cv-overview.model';
import { CalculationModel } from '../calculations/calculation.model';
import { RolePointsModel } from './detail-view/RolePointsModel';


@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';

  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL)
      .pipe(map((dtos) => dtos.map((dto) => this.mapDates(dto))));
  }

  getMemberById(id: number): Observable<MemberModel> {
    return this.httpClient.get<MemberModel>(`${this.API_URL}/${id}`)
      .pipe(map((dto) => this.mapDates(dto)));
  }

  addMember(member: MemberModel): Observable<MemberModel> {
    return this.httpClient.post<MemberModel>(this.API_URL, this.toDto(member))
      .pipe(map((dto) => this.mapDates(dto)));
  }

  updateMember(id: number, member: MemberModel): Observable<MemberModel> {
    return this.httpClient.put<MemberModel>(`${this.API_URL}/${id}`, this.toDto(member))
      .pipe(map((dto) => this.mapDates(dto)));
  }

  getCalculationsByMemberIdAndOptionalRoleId(memberId: number, roleId?: number): Observable<CalculationModel[]> {
    let roleIdParam = new HttpParams();
    if (roleId) {
      roleIdParam = roleIdParam.set('roleId', roleId);
    }
    return this.httpClient.get<CalculationModel[]>(`${this.API_URL}/${memberId}/calculations`, {
      params: roleIdParam
    });
  }

  private mapDates(dto: MemberModel): MemberModel {
    return {
      ...dto,
      birthDate: dto.birthDate ? new Date(dto.birthDate) : null!,
      dateOfHire: dto.dateOfHire ? new Date(dto.dateOfHire) : null!
    };
  }

  toDto(model: MemberModel): MemberDto {
    return {
      firstName: model.firstName,
      lastName: model.lastName,
      birthDate: DateTime.fromJSDate(model.birthDate)
        .toISODate(),
      abbreviation: model.abbreviation,
      email: model.email?.trim() === '' ? null : model.email,
      employmentState: model.employmentState,
      organisationUnitId: model.organisationUnit?.id,
      dateOfHire: model.dateOfHire ? DateTime.fromJSDate(model.dateOfHire)
        .toISODate() : null
    };
  }

  getMemberOverviewByMemberId(id: number): Observable<MemberCvOverviewModel> {
    return this.httpClient.get<MemberCvOverviewModel>(`api/v1/member-overviews/${id}`);
  }

  getPointsForActiveCalculationsForRoleByMemberId(id: number): Observable<RolePointsModel[]> {
    return this.httpClient.get<RolePointsModel[]>(`${this.API_URL}/${id}/role-points`);
  }
}
