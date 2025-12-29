import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth.service';
import { MemberService } from '../../../features/member/member.service';
import { map } from 'rxjs';


export const authGuard = (config: { scope: 'admin' | 'user' } = { scope: 'admin' }): CanActivateFn => {
  return (route, state) => {
    const userService = inject(AuthService);
    const memberService = inject(MemberService);
    const router = inject(Router);

    if (config.scope === 'user') {
      return true;
    }

    if (config.scope === 'admin' && userService.isAdmin()) {
      return true;
    }

    return memberService.getMyself()
      .pipe(map((member) => {
        const targetUrl = `/member/${member.id}`;

        if (state.url === targetUrl) {
          return true;
        }

        return router.parseUrl(targetUrl);
      }));
  };
};
