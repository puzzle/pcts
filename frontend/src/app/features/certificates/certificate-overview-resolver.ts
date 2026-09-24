import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs/operators';
import { CertificateTypeModel } from './certificate-type/certificate-type.model';
import { CertificateOverviewModel } from './certificate-overview/certificateOverviewModel';
import { CertificateTypeService } from './certificate-type/certificate-type.service';

export const certificateOverviewResolver: ResolveFn<CertificateOverviewModel[]> = (route, state) => {
  const certificateTypeService = inject(CertificateTypeService);

  return certificateTypeService.getAllCertificateTypes()
    .pipe(map((certificates: CertificateTypeModel[]) => certificates.map(mapToOverviewModel)));
};

const mapToOverviewModel = (certificate: CertificateTypeModel): CertificateOverviewModel => {
  return {
    id: certificate.id,
    points: certificate.points,
    name: certificate.name,
    publisher: certificate.publisher,
    tags: certificate.tags
  };
};
