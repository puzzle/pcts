import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: '',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}`,
    requireHttps: false,
    scope: 'openid profile',
    clientId: 'pitc_pcts_frontend',
    responseType: 'code',
    showDebugInformation: true
  } as AuthConfig
};
