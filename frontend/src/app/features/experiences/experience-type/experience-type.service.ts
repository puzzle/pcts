import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ExperienceTypeModel } from './experience-type.model';

@Injectable({
  providedIn: 'root'
})
export class ExperienceTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/experience-types';

  getAllExperienceTypes(): Observable<ExperienceTypeModel[]> {
    return this.httpClient.get<ExperienceTypeModel[]>(this.API_URL);
  }
}
