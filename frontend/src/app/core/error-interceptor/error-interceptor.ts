import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { SnackbarService } from '../toast/snackbar.service';

export const errorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>,
  next: HttpHandlerFn) => {
  const injector: Injector = inject(Injector);
  const toastService: SnackbarService = injector.get(SnackbarService);

  return next(req)
    .pipe(catchError((error: HttpErrorResponse) => {
      const translate: ScopedTranslationService = injector.get(ScopedTranslationService);
      let toasts: string[];

      if (Array.isArray(error.error)) {
        toasts = error.error.map((err) => {
          const key = `ERROR.${err.key}`;
          const values = { ...err.values };

          if (typeof values.IS === 'string') {
            const original: string = values.IS;
            values.IS = original.length > 15 ? original.slice(0, 15) + '...' : original;
          }

          if (typeof values.FIELD === 'string') {
            values.FIELD = translate.instant(`${toScreamingSnake(values.CLASS)}.${toScreamingSnake(values.FIELD)}`);
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
    .replace(/([a-z])([A-Z])/g, '$1_$2')
    .toUpperCase();
}
