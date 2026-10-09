import { ActivatedRouteSnapshot, ResolveFn } from '@angular/router';

export const certificateOverviewResolver: ResolveFn<string | null> = (route: ActivatedRouteSnapshot) => {
  return route.queryParamMap.get('q');
};
