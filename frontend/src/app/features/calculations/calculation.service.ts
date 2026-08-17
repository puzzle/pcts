import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RolePointsModel } from '../member/detail-view/RolePointsModel';

@Injectable({
  providedIn: 'root'
})
export class CalculationService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/calculations';

  getAllCalculationsByRoleId(roleId: number): Observable<RolePointsModel[]> {
    return this.httpClient.get<RolePointsModel[]>(`${this.API_URL}/${roleId}/calcs`);
  }
}
