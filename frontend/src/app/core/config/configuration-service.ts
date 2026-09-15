import { Injectable } from '@angular/core';
import { EnvironmentModel } from './environment.model';
import { environment } from '../../../environments/environment';

declare global {
  interface Window {
    APP_CONFIG?: EnvironmentModel;
  }
}

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {
  private readonly config: EnvironmentModel;

  constructor() {
    this.config = {
      production: environment.production,
      apiUrl: environment.apiUrl,
      keycloak: environment.keycloak
    };

    if (window.APP_CONFIG) {
      const cleanEntries = Object.entries((window as any).APP_CONFIG)
        .filter(([_,
          value]) => value !== null && value !== undefined);
      Object.assign(this.config, Object.fromEntries(cleanEntries));
    }
  }

  getKeyCloakUrl(): string {
    return this.config.keycloak.url;
  }
}
