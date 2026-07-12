export interface ConfigurationModel {
  keycloak: {
    url: string;
    realm: string;
    clientId: string;
    adminAuthorities: string[];
  };
}
