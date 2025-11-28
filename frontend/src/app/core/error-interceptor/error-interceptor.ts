import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ToastService } from '../toast/snackbar.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const injector = inject(Injector);
  const toastService = injector.get(ToastService);

  return next(req)
    .pipe(catchError((error: HttpErrorResponse) => {
      const translate = injector.get(ScopedTranslationService);
      console.log(error.message);
      const toasts = Array.isArray(error.error)
        ? error.error.map((err) => {
          console.log(err.values);
          const key = `ERROR.${err.key}`;
          const message = translate.instant(key, err.values || {});
          return {
            message:
              message && message !== key
                ? message
                : translate.instant('ERROR.DEFAULT')
          };
        })
        : [{
          message: translate.instant('ERROR.DEFAULT')
        }];

      toastService.showToasts(toasts, 'error');

      return throwError(() => error);
    }));
};
