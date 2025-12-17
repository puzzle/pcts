import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/i18n-prefix.provider';

export const errorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>,
  next: HttpHandlerFn) => {
  const injector: Injector = inject(Injector);
  const toastService: SnackbarService = injector.get(SnackbarService);

  return next(req)
    .pipe(catchError((error: HttpErrorResponse) => {
      const translate = injector.get(ScopedTranslationService);
      let toasts: string[];

      if (error.status === 403) {
        toasts = [translate.instant('ERROR.NOT_ALLOWED')];
        toastService.showToasts(toasts, 'error');
        return throwError(() => error);
      }

      if (Array.isArray(error.error)) {
        toasts = error.error.map((err) => {
          const key = `ERROR.${err.key}`;
          const values = { ...err.values };

          if (typeof values.IS === 'string') {
            const original: string = values.IS;
            values.IS = original.length > 15 ? original.slice(0, 15) + '...' : original;
          }

          if (typeof values.FIELD === 'string') {
            const parent = values.CLASS ?? values.ENTITY;
            values.FIELD = translate.instant(`${toScreamingSnake(parent)}.${toScreamingSnake(values.FIELD)}`);
          }

          if (typeof values.CLASS === 'string') {
            values.CLASS = translate.instant(`${toScreamingSnake(values.CLASS)}.MODEL_NAME`);
          }

          const message: string = translate.instant(key, values);
          return message && message !== key ? message : translate.instant('ERROR.DEFAULT');
        });
      } else {
        toasts = [translate.instant('ERROR.DEFAULT')];
      }

      toastService.showToasts(toasts, 'error');
      return throwError(() => error);
    }));
};

function toScreamingSnake(text: string): string {
  if (!text) {
    return '';
  }
  return text
    .replaceAll(/([a-z])([A-Z])/g, '$1_$2')
    .toUpperCase();
}
