import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

export interface MemberOverviewParams {
  searchText: string;
  statuses: string[];
}

@Injectable({ providedIn: 'root' })
export class MemberOverviewResolver implements Resolve<MemberOverviewParams> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MemberOverviewParams {
    const q = route.queryParams['q'] ? decodeURIComponent(route.queryParams['q']) : '';
    const status = route.queryParams['status'] ? decodeURIComponent(route.queryParams['status']) : '';

    const statuses = status ? status.split('+') : [];
    return {
      searchText: q,
      statuses
    };
  }
}
