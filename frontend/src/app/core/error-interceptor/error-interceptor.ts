import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/i18n-prefix.provider';
import { formatDateLocale, isSimpleISODate } from '../../shared/format/date-format';

export const errorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>,
  next: HttpHandlerFn) => {
  const injector: Injector = inject(Injector);
  const toastService: SnackbarService = injector.get(SnackbarService);
  const keysWithPotentialDates = ['IS',
    'MAX',
    'MIN'];


  return next(req)
    .pipe(catchError((error: HttpErrorResponse) => {
      const translate = injector.get(ScopedTranslationService);
      let toasts: string[];

      if (Array.isArray(error.error)) {
        toasts = error.error.map((err) => {
          const key = `ERROR.${err.key}`;
          let values = { ...err.values };

          values = formatDates(values, keysWithPotentialDates);

          if (typeof values.IS === 'string') {
            const original: string = values.IS;
            original.length > 15 ? values.IS = original.slice(0, 15) + '...' : values.IS = original;
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

/**
 * Format all values to look like dates, to use the correct date format.
 * All other values will return unchanged
 *
 * @param values object with a max depth of one
 * @param keys name of the keys to check as a list of strings
 */
function formatDates<T>(values: T, keys: (keyof T)[]): T {
  const formatted: any = { ...values };

  for (const key of keys) {
    const value = formatted[key];
    if (typeof value === 'string' && isSimpleISODate(value)) {
      formatted[key] = formatDateLocale(new Date(value));
    }
  }

  return formatted;
}

function toScreamingSnake(text: string): string {
  if (!text) {
    return '';
  }
  return text
    .replaceAll(/([a-z])([A-Z])/g, '$1_$2')
    .toUpperCase();
}
