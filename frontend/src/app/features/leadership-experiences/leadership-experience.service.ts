import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LeadershipExperienceModel } from './leadership-experience.model';
import { Observable } from 'rxjs';
import { LeadershipExperienceDto } from './dto/leadership-experience.dto';

@Injectable({
  providedIn: 'root'
})
export class LeadershipExperienceService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/leadership-experiences';

  getLeadershipExperienceById(id: number): Observable<LeadershipExperienceModel> {
    return this.httpClient.get<LeadershipExperienceModel>(`${this.API_URL}/${id}`);
  }

  addLeadershipExperience(leadershipExperience: LeadershipExperienceModel): Observable<LeadershipExperienceModel> {
    return this.httpClient.post<LeadershipExperienceModel>(this.API_URL, this.toDto(leadershipExperience));
  }

  updateLeadershipExperience(id: number, leadershipExperience: LeadershipExperienceModel): Observable<LeadershipExperienceModel> {
    return this.httpClient.put<LeadershipExperienceModel>(`${this.API_URL}/${id}`, this.toDto(leadershipExperience));
  }

  deleteLeadershipExperience(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}/${id}`);
  }

  toDto(model: LeadershipExperienceModel): LeadershipExperienceDto {
    return {
      memberId: model.member.id,
      leadershipExperienceTypeId: model.experience.id,
      comment: model.comment
    };
  }
}
