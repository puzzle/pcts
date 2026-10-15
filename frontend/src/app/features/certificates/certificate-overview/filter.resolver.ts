import { ResolveFn } from '@angular/router';
import { CertificateFilter } from './certificateFilter';

export const filterResolver: ResolveFn<CertificateFilter> = (route) => {
  const search = route.queryParamMap.get('textSearch');
  const tags = route.queryParamMap.getAll('tag');
  return { textSearch: search,
    tags: tags };
};
