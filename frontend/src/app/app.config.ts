import {
  ApplicationConfig, importProvidersFrom, inject, LOCALE_ID, provideAppInitializer,
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import localeDeCH from '@angular/common/locales/de-CH';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideTranslateService, TranslateService } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { lastValueFrom } from 'rxjs';
import { provideDateFnsAdapter } from '@angular/material-date-fns-adapter';
import { de } from 'date-fns/locale';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { errorInterceptor } from './core/error-interceptor/error-interceptor';
import { successInterceptor } from './core/success-interceptor/success-interceptor';
import { Settings } from 'luxon';
import { setDefaultOptions } from 'date-fns';

registerLocaleData(localeDeCH);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes, withComponentInputBinding()),
    importProvidersFrom(MatSnackBarModule),
    provideHttpClient(withInterceptors([errorInterceptor,
      successInterceptor])),
    provideTranslateService({
      fallbackLang: 'de',
      loader: provideTranslateHttpLoader({
        prefix: '/i18n/',
        suffix: '.json'
      })
    }),
    provideDateFnsAdapter(),
    {
      provide: MAT_DATE_LOCALE,
      useValue: de
    },
    provideAppInitializer(() => {
      const translate = inject(TranslateService);
      const lang = 'de';
      return lastValueFrom(translate.use(lang));
    }),
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline' }
    },
    {
      provide: LOCALE_ID,
      useValue: 'de-CH'
    },
    provideAppInitializer(() => {
      Settings.defaultLocale = inject(LOCALE_ID);
      setDefaultOptions({ locale: de });
    })

  ]
};
