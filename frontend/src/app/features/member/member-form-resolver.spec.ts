import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { memberDataResolver } from './member-form-resolver';

describe('MemberFormResolver', () => {
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  const mockRoute = (id: string | null) => {
    return {
      paramMap: {
        get: (key: string) => {
          return key === 'id' ? id : null;
        }
      }
    } as unknown as ActivatedRouteSnapshot;
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: []
    });
    route = new ActivatedRouteSnapshot();
    state = {} as RouterStateSnapshot;
  });

  it('Should create', () => {
    expect(route)
      .toBeTruthy();
  });

  it('should return id', () => {
    const testId = '1';

    route = mockRoute(testId);

    const result = memberDataResolver(route, state);

    expect(result)
      .toBe(testId);
  });

  it('Should throw error when id is missing', () => {
    route = mockRoute(null);

    expect(() => memberDataResolver(route, state))
      .toThrow(new Error('ID param is missing'));
  });
});
