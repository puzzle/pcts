import { ResolveFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

export interface MemberOverviewParams {
  searchText: string;
  statuses: string[];
}

export const memberOverviewResolver: ResolveFn<MemberOverviewParams> =
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    const q = route.queryParams['q']
      ? decodeURIComponent(route.queryParams['q'])
      : '';
    const status = route.queryParams['status']
      ? decodeURIComponent(route.queryParams['status'])
      : '';

    const statuses = status ? status.split('+') : [];

    return {
      searchText: q,
      statuses
    };
  };
