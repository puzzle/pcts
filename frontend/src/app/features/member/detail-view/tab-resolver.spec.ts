import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, convertToParamMap, RouterStateSnapshot } from '@angular/router';
import { tabResolver } from './tab-resolver';

const mockRoute = (tabIndex: string | null): ActivatedRouteSnapshot => ({
  queryParamMap: convertToParamMap(tabIndex ? { tabIndex } : {})
} as ActivatedRouteSnapshot);

describe('tabResolver', () => {
  let state: RouterStateSnapshot;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    state = {} as RouterStateSnapshot;
  });

  it('should return 0 when tabIndex is null', () => {
    const route = mockRoute(null);

    const result = TestBed.runInInjectionContext(() => tabResolver(route, state));

    expect(result)
      .toBe(0);
  });

  it('should return the tabIndex as a number when it is valid', () => {
    const route = mockRoute('3');

    const result = TestBed.runInInjectionContext(() => tabResolver(route, state));

    expect(result)
      .toBe(3);
  });

  it('should return 0 when tabIndex is not a number', () => {
    const route = mockRoute('abc');

    const result = TestBed.runInInjectionContext(() => tabResolver(route, state));

    expect(result)
      .toBe(0);
  });
});
