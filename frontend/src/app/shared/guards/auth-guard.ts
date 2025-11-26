import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route, state) => {
  const oAuthService = inject(OAuthService);

  return oAuthService.loadDiscoveryDocumentAndTryLogin()
    .then(async() => {
      const hasValidToken = oAuthService.hasValidIdToken();
      if (!hasValidToken) {
        oAuthService.initCodeFlow();
        return false;
      }
      oAuthService.setupAutomaticSilentRefresh();
      return true;
    });
};
