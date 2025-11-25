import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CertificateModel } from './certificate.model';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/certificates';

  getCertificateById(id: number): Observable<CertificateModel> {
    return this.httpClient.get<CertificateModel>(`${this.API_URL}/${id}`);
  }

  addCertificate(certificate: CertificateModel): Observable<CertificateModel> {
    return this.httpClient.post<CertificateModel>(this.API_URL, certificate);
  }

  updateCertificate(id: number, certificate: CertificateModel): Observable<CertificateModel> {
    return this.httpClient.put<CertificateModel>(`${this.API_URL}/${id}`, certificate);
  }

  deleteCertificate(id: number) {
    this.httpClient.delete(`${this.API_URL}/${id}`);
  }
}
