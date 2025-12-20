import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { of } from 'rxjs';

import { authGuard } from './auth.guard';
import { UserService } from '../user.service';
import { MemberService } from '../../../features/member/member.service';
import { APP_CONFIG } from '../../../features/configuration/configuration.token';

describe('authGuard', () => {
  let userServiceMock: jest.Mocked<UserService>;
  let memberServiceMock: jest.Mocked<MemberService>;
  let routerMock: jest.Mocked<Router>;
  const mockConfig = { adminAuthorities: ['ADMIN_ROLE'] };

  const executeGuard = (config?: { scope: 'admin' | 'user' },
    route: Partial<ActivatedRouteSnapshot> = {},
    state: Partial<RouterStateSnapshot> = { url: '/test' }) => {
    return TestBed.runInInjectionContext(() => authGuard(config)(route as ActivatedRouteSnapshot, state as RouterStateSnapshot));
  };

  beforeEach(() => {
    userServiceMock = {
      getRoles: jest.fn()
    } as any;

    memberServiceMock = {
      getMyself: jest.fn()
    } as any;

    routerMock = {
      parseUrl: jest.fn((url) => ({ toString: () => url } as UrlTree))
    } as any;

    TestBed.configureTestingModule({
      providers: [
        { provide: UserService,
          useValue: userServiceMock },
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: Router,
          useValue: routerMock },
        { provide: APP_CONFIG,
          useValue: mockConfig }
      ]
    });
  });

  it('should allow access if scope is "user"', () => {
    const result = executeGuard({ scope: 'user' });
    expect(result)
      .toBe(true);
  });

  it('should allow access if scope is "admin" and user has admin role', () => {
    userServiceMock.getRoles.mockReturnValue(['ADMIN_ROLE']);

    const result = executeGuard({ scope: 'admin' });

    expect(result)
      .toBe(true);
  });

  describe('Non-Admin redirection', () => {
    beforeEach(() => {
      userServiceMock.getRoles.mockReturnValue(['USER_ROLE']); // Not an admin
    });

    it('should redirect to /member/:id if user is not an admin', (done) => {
      memberServiceMock.getMyself.mockReturnValue(of({ id: 7 } as any));

      const result$ = executeGuard({ scope: 'admin' });

      if (result$ instanceof typeof of) {
        (result$ as any).subscribe((_: UrlTree) => {
          expect(routerMock.parseUrl)
            .toHaveBeenCalledWith('/member/7');
          done();
        });
      }
    });

    it('should allow access if non-admin is already on their target personal URL', (done) => {
      memberServiceMock.getMyself.mockReturnValue(of({ id: 7 } as any));

      const result$ = executeGuard({ scope: 'admin' }, {}, { url: '/member/7' });

      (result$ as any).subscribe((res: boolean) => {
        expect(res)
          .toBe(true);
        expect(routerMock.parseUrl).not.toHaveBeenCalled();
        done();
      });
    });
  });
});
