import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { CertificateTypeModel } from './certificate-type/certificate-type.model';
import { CertificateTypeService } from './certificate-type/certificate-type.service';

export const certificateTypeResolver: ResolveFn<CertificateTypeModel[]> = () => {
  const certificateTypeService = inject(CertificateTypeService);

  return certificateTypeService.getAllCertificateTypes();
};
