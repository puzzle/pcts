import { inject, Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { PuzzleTokenModel } from './puzzle-token.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly keycloak = inject(Keycloak);

  getRoles(): string[] {
    const parsedToken = this.keycloak.tokenParsed as PuzzleTokenModel | undefined;

    if (!parsedToken) {
      return [];
    }

    return parsedToken.pitc.roles ? parsedToken.pitc.roles : [];
  }

  getName(): string | null {
    const parsedToken = this.keycloak.tokenParsed as PuzzleTokenModel | undefined;

    if (!parsedToken) {
      return null;
    }

    if (parsedToken.name) {
      return parsedToken.name;
    }

    if (parsedToken.given_name && parsedToken.family_name) {
      return `${parsedToken.given_name} ${parsedToken.family_name}`;
    }

    if (parsedToken.preferred_username) {
      return parsedToken.preferred_username;
    }

    return null;
  }

  logout(): void {
    this.keycloak.logout();
  }
}
