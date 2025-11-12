import { ResolveFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';

export interface MemberOverviewParams {
  searchText: string;
  statuses: Set<EmploymentState>;
}

export const memberOverviewResolver: ResolveFn<MemberOverviewParams> =
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    const q = route.queryParamMap.get('q') ?? '';
    const statuses = new Set(route.queryParamMap.getAll('status') as EmploymentState[]);

    return {
      searchText: q,
      statuses
    };
  };
