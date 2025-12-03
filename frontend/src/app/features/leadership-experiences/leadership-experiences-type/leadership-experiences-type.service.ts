import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class LeadershipExperiencesTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/leadership-experiences-types';

  getAllLeadershipTypes(): Observable<LeadershipExperiencesTypeService[]> {
    return this.httpClient.get<LeadershipExperiencesTypeService[]>(this.API_URL);
  }
}
