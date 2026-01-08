import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { lastValueFrom, of } from 'rxjs';

import { authGuard } from './auth.guard';
import { AuthService } from '../auth.service';
import { MemberService } from '../../../features/member/member.service';
import { APP_CONFIG } from '../../../features/configuration/configuration.token';

describe('authGuard', () => {
  let userServiceMock: Partial<AuthService>;
  let memberServiceMock: Partial<MemberService>;
  let routerMock: Partial<Router>;
  const mockConfig = { adminAuthorities: ['ADMIN_ROLE'] };

  const executeGuard = (config?: { scope: 'admin' | 'user' },
    route: Partial<ActivatedRouteSnapshot> = {},
    state: Partial<RouterStateSnapshot> = { url: '/test' }) => {
    return TestBed.runInInjectionContext(() => authGuard(config)(route as ActivatedRouteSnapshot, state as RouterStateSnapshot));
  };

  beforeEach(() => {
    jest.resetAllMocks();

    userServiceMock = {
      getRoles: jest.fn(),
      isAdmin: jest.fn()
    };

    memberServiceMock = {
      getMyself: jest.fn()
        .mockReturnValue(of({ id: 7 } as any))
    };

    routerMock = {
      parseUrl: jest.fn((url) => ({ toString: () => url } as UrlTree))
    };

    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: userServiceMock
        },
        {
          provide: MemberService,
          useValue: memberServiceMock
        },
        {
          provide: Router,
          useValue: routerMock
        },
        {
          provide: APP_CONFIG,
          useValue: mockConfig
        }
      ]
    });
  });

  it('should allow access if scope is "user"', () => {
    const result = executeGuard({ scope: 'user' });
    expect(result)
      .toBe(true);
  });

  it('should allow access if scope is "admin" and user has admin role', () => {
    (userServiceMock.isAdmin as jest.Mock).mockReturnValue(true);

    const result = executeGuard({ scope: 'admin' });

    expect(result)
      .toBe(true);
  });

  describe('Non-Admin redirection', () => {
    beforeEach(() => {
      userServiceMock = {
        getRoles: jest.fn()
          .mockReturnValue(['USER_ROLE'])
      };
    });

    it('should redirect to /member/:id if user is not an admin', async() => {
      const result$ = executeGuard({ scope: 'admin' });

      await lastValueFrom(result$);

      expect(routerMock.parseUrl)
        .toHaveBeenCalledWith('/member/7');
    });

    it('should allow access if non-admin is already on their target personal URL', async() => {
      const result$ = executeGuard({ scope: 'admin' }, {}, { url: '/member/7' });

      const result = await lastValueFrom(result$);

      expect(result)
        .toBe(true);
      expect(routerMock.parseUrl).not.toHaveBeenCalled();
    });
  });
});
