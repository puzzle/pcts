import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalculationModel } from './calculation.model';

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
    return this.httpClient.put<CalculationModel>(`${this.API_URL}/${id}`, calculation);
  }

  addCalculation(calculation: CalculationModel): Observable<CalculationModel> {
    return this.httpClient.post<CalculationModel>(`${this.API_URL}`, calculation);
  }
}
