import { ResolveFn } from '@angular/router';

export const filterResolver: ResolveFn<{ textSearch: string } | null> = (route) => {
  const search = route.queryParamMap.get('q');
  return search ? { textSearch: search } : null;
};
