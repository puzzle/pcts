import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { memberIdResolver } from './member-id-resolver';

describe('memberIdResolver', () => {
  let mockRoute: ActivatedRouteSnapshot;
  let mockState: RouterStateSnapshot;

  beforeEach(() => {
    mockRoute = {
      paramMap: {
        get: jest.fn()
      }
    } as unknown as ActivatedRouteSnapshot;

    mockState = {} as RouterStateSnapshot;
  });

  it('should return the id as number', () => {
    const id = 1;
    (mockRoute.paramMap.get as jest.Mock).mockReturnValue(id.toString());

    const result = memberIdResolver(mockRoute, mockState);

    expect(mockRoute.paramMap.get)
      .toHaveBeenCalledTimes(1);
    expect(result)
      .toEqual(id);
  });

  it('should throw a error if id is missing', () => {
    (mockRoute.paramMap.get as jest.Mock).mockReturnValue(null);

    expect(() => memberIdResolver(mockRoute, mockState))
      .toThrow('Member id is missing in the route');
  });
});
