import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CertificateModel } from './certificate.model';
import { CertificateDto } from './dto/certificate.dto';
import { DateTime } from 'luxon';


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
    return this.httpClient.post<CertificateModel>(this.API_URL, this.toDto(certificate));
  }

  updateCertificate(id: number, certificate: CertificateDto): Observable<CertificateModel> {
    return this.httpClient.put<CertificateModel>(`${this.API_URL}/${id}`, certificate);
  }

  deleteCertificate(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}/${id}`);
  }

  toDto(model: CertificateModel): CertificateDto {
    return {
      memberId: model.member.id,
      certificateTypeId: model.certificateType.id,
      completedAt: DateTime.fromJSDate(model.completedAt)
        .toISODate(),
      validUntil: model.validUntil ? DateTime.fromJSDate(model.validUntil)
        .toISODate() : null,
      comment: model.comment
    };
  }
}
