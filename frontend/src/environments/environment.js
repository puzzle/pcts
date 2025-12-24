export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: 'http://localhost:8544',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `http://localhost:8544`, //TODO: make this dynamic again
    requireHttps: false,
    scope: 'openid profile',
    clientId: '',
    responseType: 'code',
    showDebugInformation: true,
  },
}
