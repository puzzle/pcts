import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalculationModel } from './calculation.model';
import { CalculationInputDto } from './dto/calculation.dto';
import { CertificateCalculationInputDto } from './certificate-calculation/dto/certificate-calculation.dto';
import {
  LeadershipExperienceCalculationInputDto
} from './leadership-experience-calculation/dto/leadership-experience-calculation.dto';
import { DegreeCalculationInputDto } from './degree-calculation/dto/degree-calculation.dto';
import { ExperienceCalculationInputDto } from './experience-calculation/dto/experience-calculation.dto';

@Injectable({
  providedIn: 'root'
})
export class CalculationService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/calculations';

  getCalculationById(id: number): Observable<CalculationModel> {
    return this.httpClient.get<CalculationModel>(`${this.API_URL}/${id}`);
  }

  updateCalculation(id: number, calculation: CalculationModel): Observable<CalculationModel> {
    return this.httpClient.put<CalculationModel>(`${this.API_URL}/${id}`, this.toDto(calculation));
  }

  getTemplate(memberId: number, roleId: number): Observable<CalculationModel> {
    return this.httpClient.get<CalculationModel>(`${this.API_URL}/template/${memberId}/${roleId}`);
    // return this.httpClient.put<CalculationModel>(`${this.API_URL}/${id}`, this.toDto(calculation));
  }

  addCalculation(calculation: CalculationModel): Observable<CalculationModel> {
    return this.httpClient.post<CalculationModel>(`${this.API_URL}`, this.toDto(calculation));
  }

  toDto(model: CalculationModel): CalculationInputDto {
    const certificateCalculationsDtos: CertificateCalculationInputDto[] = model.certificateCalculations.map((model) => {
      return {
        id: model.id,
        certificateId: model.certificate.id
      };
    });

    const leadershipExperienceCalculationsDtos: LeadershipExperienceCalculationInputDto[] = model.leadershipExperienceCalculations.map((model) => {
      return {
        id: model.id,
        leadershipExperienceId: model.leadershipExperience.id
      };
    });

    const degreeCalculationsDtos: DegreeCalculationInputDto[] = model.degreeCalculations.map((model) => {
      return {
        id: model.id,
        degreeId: model.degree.id,
        relevancies: model.relevancies,
        comment: model.comment
      };
    });

    const experienceCalculationsDtos: ExperienceCalculationInputDto[] = model.experienceCalculations.map((model) => {
      return {
        id: model.id,
        experienceId: model.experience.id,
        relevancy: model.relevancy,
        comment: model.comment
      };
    });

    return {
      memberId: model.member.id,
      state: model.state,
      roleId: model.role.id,
      certificateCalculations: certificateCalculationsDtos,
      leadershipExperienceCalculations: leadershipExperienceCalculationsDtos,
      degreeCalculations: degreeCalculationsDtos,
      experienceCalculations: experienceCalculationsDtos
    };
  }
}
