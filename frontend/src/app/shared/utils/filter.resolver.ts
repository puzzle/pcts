import { ResolveFn } from '@angular/router';
import { CertificateFilter } from '../../features/certificates/certificate-overview/certificateFilter';

export const filterResolver: ResolveFn<CertificateFilter> = (route) => {
  const search = route.queryParamMap.get('q');
  return { textSearch: search };
};
