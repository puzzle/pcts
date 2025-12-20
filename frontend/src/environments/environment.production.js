export const environment = {
  production: true,
  apiUrl: 'http://pcts-backend:8080',
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: 'https://sso.puzzle.ch/auth/realms/pitc',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}`,
    scope: 'profile openid',
    clientId: 'pitc_pcts_prod',
    responseType: 'code',
    showDebugInformation: false,
  },
}
