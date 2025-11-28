import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { tap } from 'rxjs/operators';

export const successInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.method === 'GET') {
    return next(req);
  }

  const injector = inject(Injector);
  const toastService = injector.get(SnackbarService);
  const translate = injector.get(ScopedTranslationService);

  return next(req)
    .pipe(tap((event) => {
      if (event instanceof HttpResponse && event.ok) {
        const objectName = req.url.split('?')[0].split('/')[3] || 'Object';
        const keyName = capitalize(singularize(objectName));
        const message = translate.instant(req.method, { OBJECT: keyName });

        toastService.showToasts([message], 'success');
      }
    }));
};

function singularize(word: string) {
  if (word.endsWith('ies')) {
    return word.slice(0, -3) + 'y';
  }
  if (word.endsWith('s')) {
    return word.slice(0, -1);
  }
  return word;
}

function capitalize(word: string) {
  return word.charAt(0)
    .toUpperCase() + word.slice(1);
}
