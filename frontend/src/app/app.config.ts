import {
  ApplicationConfig, inject, LOCALE_ID, provideAppInitializer,
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import localeDeCH from '@angular/common/locales/de-CH';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { provideTranslateService, TranslateService } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { CUSTOM_LUXON_DATE_FORMATS } from './shared/format/date-format';
import { provideLuxonDateAdapter } from '@angular/material-luxon-adapter';
import { lastValueFrom } from 'rxjs';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localeDeCH);
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(),
    provideTranslateService({
      fallbackLang: 'de',
      loader: provideTranslateHttpLoader({
        prefix: '/i18n/',
        suffix: '.json'
      })
    }),
    provideLuxonDateAdapter(),
    provideAppInitializer(() => {
      const translate = inject(TranslateService);
      const lang = 'de';
      return lastValueFrom(translate.use(lang));
    }),
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline' }
    },
    { provide: MAT_DATE_FORMATS,
      useValue: CUSTOM_LUXON_DATE_FORMATS },
    {
      provide: LOCALE_ID,
      useValue: 'de-CH'
    }
  ]
};
