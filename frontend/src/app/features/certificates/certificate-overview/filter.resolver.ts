import { ResolveFn } from '@angular/router';
import { CertificateFilter } from './certificateFilter';

export const filterResolver: ResolveFn<CertificateFilter> = (route) => {
  const search = route.queryParamMap.get('textSearch');
  return { textSearch: search };
};
