import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LeadershipExperienceTypeModel } from './leadership-experience-type.model';

@Injectable({
  providedIn: 'root'
})

export class LeadershipExperienceTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/leadership-experience-types';

  getAllLeadershipExperienceTypes(): Observable<LeadershipExperienceTypeModel[]> {
    return this.httpClient.get<LeadershipExperienceTypeModel[]>(this.API_URL);
  }
}
