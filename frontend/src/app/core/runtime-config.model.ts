import { InjectionToken } from '@angular/core';

export interface RuntimeConfig {
  apiUrl: string;
  keycloak: {
    url: string;
    realm: string;
    clientId: string;
    adminAuthorities: string[];
  };
}

export const RUNTIME_CONFIG = new InjectionToken<RuntimeConfig>('RUNTIME_CONFIG');
