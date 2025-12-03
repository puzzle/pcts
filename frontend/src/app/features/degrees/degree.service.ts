import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DateTime } from 'luxon';
import { DegreeModel } from './degree.model';
import { DegreeDto } from './dto/degree.dto';

@Injectable({
  providedIn: 'root'
})
export class DegreeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/degrees';

  getDegreeById(id: number): Observable<DegreeModel> {
    return this.httpClient.get<DegreeModel>(`${this.API_URL}/${id}`);
  }

  addDegree(degree: DegreeModel): Observable<DegreeModel> {
    return this.httpClient.post<DegreeModel>(this.API_URL, this.toDto(degree));
  }

  updateDegree(id: number, degree: DegreeModel): Observable<DegreeModel> {
    return this.httpClient.put<DegreeModel>(`${this.API_URL}/${id}`, this.toDto(degree));
  }

  deleteDegree(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}/${id}`);
  }

  toDto(model: DegreeModel): DegreeDto {
    return {
      name: model.name,
      memberId: model.member.id,
      degreeTypeId: model.degreeType.id,
      institution: model.institution,
      completed: model.completed,
      comment: model.comment,
      startDate: DateTime.fromJSDate(model.startDate)
        .toISODate(),
      endDate: model.endDate ? DateTime.fromJSDate(model.endDate)
        .toISODate() : null
    };
  }
}
