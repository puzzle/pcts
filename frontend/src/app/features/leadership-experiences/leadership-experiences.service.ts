import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LeadershipExperiencesModel } from './leadership-experiences.model';
import { Observable } from 'rxjs';
import { LeadershipExperiencesDto } from './dto/leadership-experiences.dto';

@Injectable({
  providedIn: 'root'
})
export class LeadershipExperiencesService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/leadership-experiences';

  getLeadershipExperienceById(id: number): Observable<LeadershipExperiencesModel> {
    return this.httpClient.get<LeadershipExperiencesModel>(`${this.API_URL}/${id}`);
  }

  addLeadershipExperience(leadershipExperience: LeadershipExperiencesModel): Observable<LeadershipExperiencesModel> {
    return this.httpClient.post<LeadershipExperiencesModel>(this.API_URL, this.toDto(leadershipExperience));
  }

  updateLeadershipExperience(id: number, leadershipExperience: LeadershipExperiencesModel): Observable<LeadershipExperiencesModel> {
    return this.httpClient.put<LeadershipExperiencesModel>(`${this.API_URL}/${id}`, this.toDto(leadershipExperience));
  }

  deleteLeadershipExperience(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}/${id}`);
  }

  toDto(model: LeadershipExperiencesModel): LeadershipExperiencesDto {
    return {
      memberId: model.member.id,
      leadershipExperienceTypeId: model.experienceType.id,
      comment: model.comment
    };
  }
}
