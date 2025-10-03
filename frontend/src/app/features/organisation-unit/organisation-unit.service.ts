import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { OrganisationUnitModel } from './organisation-unit.model';

@Injectable({
  providedIn: 'root'
})
export class OrganisationUnitService {
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
}
