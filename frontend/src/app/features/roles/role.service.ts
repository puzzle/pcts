import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleModel } from './RoleModel';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/roles';

  getAllRoles(): Observable<RoleModel[]> {
    return this.httpClient.get<RoleModel[]>(this.API_URL);
  }
}
