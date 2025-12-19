import { CanActivateChildFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './user.service';


export const authGuard = (config: { redirectTo: string;
  scope: 'admin' | 'user'; } = { redirectTo: '/members',
  scope: 'admin' }): CanActivateChildFn => {
  return (route, state) => {
    const userService = inject(UserService);
    const router = inject(Router);

    // TODO: make this dynamic
    const adminRoles = ['org_hr',
      'org_gl'];

    const roles = userService.getRoles();

    if (config.scope === 'admin') {
      return roles.some((role) => adminRoles.includes(role));
    }

    return router.parseUrl(config.redirectTo);
  };
};
