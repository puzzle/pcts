import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DateTime } from 'luxon';
import { ExperienceModel } from './experience.model';
import { ExperienceDto } from './dto/experience.dto';

@Injectable({
  providedIn: 'root'
})
export class ExperienceService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/experiences';

  getExperienceById(id: number): Observable<ExperienceModel> {
    return this.httpClient.get<ExperienceModel>(`${this.API_URL}/${id}`);
  }

  addExperience(experience: ExperienceModel): Observable<ExperienceModel> {
    return this.httpClient.post<ExperienceModel>(this.API_URL, this.toDto(experience));
  }

  updateExperience(id: number, experience: ExperienceModel): Observable<ExperienceModel> {
    return this.httpClient.put<ExperienceModel>(`${this.API_URL}/${id}`, this.toDto(experience));
  }

  deleteExperience(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}/${id}`);
  }

  toDto(model: ExperienceModel): ExperienceDto {
    return {
      name: model.name,
      memberId: model.member.id,
      experienceTypeId: model.experienceType.id,
      comment: model.comment,
      employer: model.employer,
      percent: model.percent,
      startDate: DateTime.fromJSDate(model.startDate)
        .toISODate(),
      endDate: model.endDate ? DateTime.fromJSDate(model.endDate)
        .toISODate() : null
    };
  }
}
