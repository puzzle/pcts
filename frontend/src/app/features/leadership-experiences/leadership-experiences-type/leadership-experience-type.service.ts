import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class LeadershipExperienceTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/leadership-experience-types';

  getAllLeadershipTypes(): Observable<LeadershipExperienceTypeService[]> {
    return this.httpClient.get<LeadershipExperienceTypeService[]>(this.API_URL);
  }
}
