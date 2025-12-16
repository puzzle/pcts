import { KeycloakTokenParsed } from 'keycloak-js';

export interface PuzzleTokenModel extends KeycloakTokenParsed {
  name?: string;
  preferred_username?: string;
  given_name?: string;
  family_name?: string;
  email?: string;
  pitc: {
    uid: string;
    roles: string[];
  };
}
