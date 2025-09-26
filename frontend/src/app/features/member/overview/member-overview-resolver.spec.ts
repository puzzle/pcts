import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { MemberOverviewResolver } from './member-overview-resolver';

describe('MemberOverviewResolver', () => {
  let resolver: MemberOverviewResolver;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MemberOverviewResolver]
    });
    resolver = TestBed.inject(MemberOverviewResolver);
    route = new ActivatedRouteSnapshot();
    state = {} as RouterStateSnapshot;
  });

  it('should create', () => {
    expect(resolver)
      .toBeTruthy();
  });

  it('should return empty params when none are given', () => {
    route.queryParams = {};
    expect(resolver.resolve(route, state))
      .toEqual({ searchText: '',
        statuses: [] });
  });

  it('should resolve q and status params', () => {
    route.queryParams = { q: encodeURIComponent('ja'),
      status: encodeURIComponent('MEMBER+EX_MEMBER') };
    const result = resolver.resolve(route, state);
    expect(result)
      .toEqual({ searchText: 'ja',
        statuses: ['MEMBER',
          'EX_MEMBER'] });
  });
});
