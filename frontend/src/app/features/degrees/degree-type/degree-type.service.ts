import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DegreeTypeModel } from './degree-type.model';

@Injectable({
  providedIn: 'root'
})
export class DegreeTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/degree-types';

  getAllDegreeTypes(): Observable<DegreeTypeModel[]> {
    return this.httpClient.get<DegreeTypeModel[]>(this.API_URL);
  }
}
