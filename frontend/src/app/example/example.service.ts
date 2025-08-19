import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ExampleDto } from './dto/example.dto';
import { CreateExampleDto } from './dto/create-example.dto';

@Injectable({
  providedIn: 'root'
})
export class ExampleService {
  private http = inject(HttpClient);

  private readonly apiUrl = '/api/v1/examples';

  getAllExamples(): Observable<ExampleDto[]> {
    return this.http.get<ExampleDto[]>(this.apiUrl);
  }

  getExampleById(id: number): Observable<ExampleDto> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<ExampleDto>(url);
  }

  createExample(exampleData: CreateExampleDto): Observable<ExampleDto> {
    return this.http.post<ExampleDto>(this.apiUrl, exampleData);
  }
}
