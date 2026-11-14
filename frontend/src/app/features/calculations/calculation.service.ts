import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { CalculationDto, CalculationModel } from './calculationDto';
import { toCalculationInputDto, toCalculationModel } from './calculation-mapper';

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
    return this.httpClient.put<CalculationModel>(`${this.API_URL}/${id}`, toCalculationInputDto(calculation));
  }

  getTemplate(memberId: number, roleId: number): Observable<CalculationModel> {
    return this.httpClient.get<CalculationDto>(`${this.API_URL}/template/${memberId}/${roleId}`)
      .pipe(map((calculationDto) => toCalculationModel(calculationDto)));
  }

  addCalculation(calculation: CalculationModel): Observable<CalculationModel> {
    return this.httpClient.post<CalculationModel>(`${this.API_URL}`, toCalculationInputDto(calculation));
  }
}
