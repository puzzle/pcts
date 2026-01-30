import { ResolveFn } from '@angular/router';

export const tabResolver: ResolveFn<number> = (route): number => {
  const tabIndex = Number(route.queryParamMap.get('tabIndex'));
  return isNaN(tabIndex) ? 0 : tabIndex;
};
