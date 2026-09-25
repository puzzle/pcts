import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CertificateTypeModel } from './certificate-type.model';

@Injectable({
  providedIn: 'root'
})
export class CertificateTypeService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/certificate-types';

  getAllCertificateTypes(): Observable<CertificateTypeModel[]> {
    return this.httpClient.get<CertificateTypeModel[]>(this.API_URL);
  }
}
