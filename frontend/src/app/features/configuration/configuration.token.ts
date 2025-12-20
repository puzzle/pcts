import { ConfigurationModel } from './configuration.model';
import { inject, InjectionToken, provideAppInitializer } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { lastValueFrom, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export const APP_CONFIG = new InjectionToken<ConfigurationModel>('APP_CONFIG');

let configState: ConfigurationModel;

export function provideConfiguration() {
  return [{
    provide: APP_CONFIG,
    useFactory: () => configState
  },
  provideAppInitializer(() => {
    const configService = inject(ConfigurationService);

    return lastValueFrom(configService.getConfiguration()
      .pipe(catchError(() => {
        console.error('Could not load configuration, using defaults.');
        return of({ adminAuthorities: [] });
      }), tap((config) => {
        configState = config;
      })));
  })];
}
