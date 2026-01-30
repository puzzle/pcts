import { ResolveFn } from '@angular/router';

export const tabResolver: ResolveFn<number> = (route): number => {
  const id = Number(route.queryParamMap.get('tabIndex'));
  return isNaN(id) ? 0 : id;
};
