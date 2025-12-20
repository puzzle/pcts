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
import { provideI18nPrefix } from './shared/i18n-prefix.provider';
import { registerLocaleData } from '@angular/common';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { errorInterceptor } from './core/error-interceptor/error-interceptor';
import { successInterceptor } from './core/success-interceptor/success-interceptor';
import {
  createInterceptorCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, IncludeBearerTokenCondition, includeBearerTokenInterceptor,
  provideKeycloak
} from 'keycloak-angular';
import { environment } from '../environments/environment';
import { provideDateFnsAdapter } from '@angular/material-date-fns-adapter';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { de } from 'date-fns/locale/de';
import { provideConfiguration } from './features/configuration/configuration.token';

registerLocaleData(localeDeCH);

const urlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^(\/)?api/,
  bearerPrefix: 'Bearer'
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideKeycloak({
      config: {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      },
      initOptions: {
        onLoad: 'login-required'
      }
    }),
    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [urlCondition]
    },
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes, withComponentInputBinding()),
    importProvidersFrom(MatSnackBarModule),
    provideHttpClient(withInterceptors([errorInterceptor,
      successInterceptor,
      includeBearerTokenInterceptor])),
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
    provideI18nPrefix(''),
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline' }
    },
    {
      provide: LOCALE_ID,
      useValue: 'de-CH'
    },
    provideConfiguration()
  ]
};
