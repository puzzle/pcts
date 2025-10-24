import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { MemberOverviewParams, memberOverviewResolver } from './member-overview-resolver';

describe('memberOverviewResolver', () => {
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  beforeEach(() => {
    route = {} as ActivatedRouteSnapshot;
    state = {} as RouterStateSnapshot;
  });

  it('should return empty searchText and statuses when no query params', () => {
    route.queryParams = {};
    const result = memberOverviewResolver(route, state);
    expect(result)
      .toEqual<MemberOverviewParams>({
        searchText: '',
        statuses: []
      });
  });

  it('should decode and return the q query param as searchText', () => {
    route.queryParams = { q: encodeURIComponent('test search') };
    const result = memberOverviewResolver(route, state);
    expect(result)
      .toEqual<MemberOverviewParams>({
        searchText: 'test search',
        statuses: []
      });
  });

  it('should decode and split status query param into statuses array', () => {
    route.queryParams = { status: encodeURIComponent('active+inactive') };
    const result = memberOverviewResolver(route, state);
    expect(result)
      .toEqual<MemberOverviewParams>({
        searchText: '',
        statuses: ['active',
          'inactive']
      });
  });

  it('should handle both q and status query params', () => {
    route.queryParams = {
      q: encodeURIComponent('search term'),
      status: encodeURIComponent('pending+active')
    };
    const result = memberOverviewResolver(route, state);
    expect(result)
      .toEqual<MemberOverviewParams>({
        searchText: 'search term',
        statuses: ['pending',
          'active']
      });
  });

  it('should return empty array for status if it is an empty string', () => {
    route.queryParams = { status: '' };
    const result = memberOverviewResolver(route, state);
    expect(result)
      .toEqual<MemberOverviewParams>({
        searchText: '',
        statuses: []
      });
  });
});
