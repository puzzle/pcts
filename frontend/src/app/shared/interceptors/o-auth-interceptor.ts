import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter, map, merge, mergeMap, of, take, timeout } from 'rxjs';
import { environment } from '../../../environments/environment'; // Adjust the path as necessary

export const oAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const oAuthService = inject(OAuthService);

  console.log(environment);

  if (!req.url.match(/^(\/)?api/)) {
    console.log('No match');
    return next(req);
  }


  console.log('Match');
  return merge(of(oAuthService.getAccessToken())
    .pipe(filter((token) => !!token)), oAuthService.events.pipe(filter((e) => e.type === 'token_received'), timeout(500), map((_) => oAuthService.getAccessToken())))
    .pipe(take(1), mergeMap((token) => {
      if (token) {
        const header = 'Bearer ' + token;
        const headers = req.headers.set('Authorization', header);
        req = req.clone({ headers });
      }

      return next(req);
    }));
};
