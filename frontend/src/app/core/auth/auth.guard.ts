import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './user.service';
import { APP_CONFIG } from '../../features/configuration/configuration.token';
import { MemberService } from '../../features/member/member.service';
import { map } from 'rxjs';


export const authGuard = (config: { scope: 'admin' | 'user' } = { scope: 'admin' }): CanActivateFn => {
  return (route, state) => {
    const userService = inject(UserService);
    const memberService = inject(MemberService);
    const router = inject(Router);
    const appConfig = inject(APP_CONFIG);

    const adminRoles = appConfig.adminAuthorities;
    const roles = userService.getRoles();

    if (config.scope === 'user') {
      return true;
    }

    const isAdmin = roles.some((role) => adminRoles.includes(role));
    if (isAdmin) {
      return true;
    }

    return memberService.getMyself()
      .pipe(map((member) => {
        const targetUrl = `/member/${member.id}`;

        if (state.url === targetUrl) {
          return true;
        }

        return router.parseUrl(`/member/${member.id}`);
      }));
  };
};
