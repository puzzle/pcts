import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ToastService } from '../snackbar.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const injector = inject(Injector);
  const toastService = injector.get(ToastService);

  return next(req)
    .pipe(catchError((error: HttpErrorResponse) => {
      const translate = injector.get(ScopedTranslationService);

      if (Array.isArray(error.error)) {
        const toastItems = error.error.map((err) => {
          const key = `ERROR.${err.key}`;
          const translated = translate.instant(key, err.values || {});

          return {
            message: translated === key
              ? translate.instant('ERROR.DEFAULT')
              : translated
          };
        });

        toastService.showToasts(toastItems);
      } else {
        toastService.showToasts([{
          message: translate.instant('ERROR.DEFAULT')
        }]);
      }

      return throwError(() => error);
    }));
};
