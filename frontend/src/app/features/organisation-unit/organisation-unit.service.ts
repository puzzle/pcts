import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrganisationUnitModel } from './organisation-unit.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrganisationUnitService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/organisation-units';

  getAllOrganisationUnits(): Observable<OrganisationUnitModel[]> {
    return this.httpClient.get<OrganisationUnitModel[]>(this.API_URL);
  }
}
