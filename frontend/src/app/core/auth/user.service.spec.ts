import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import Keycloak from 'keycloak-js';

type MockKeycloak = Partial<Keycloak> & { tokenParsed?: any };

describe('User', () => {
  let service: UserService;
  let keycloakMock: MockKeycloak;

  beforeEach(() => {
    keycloakMock = {
      tokenParsed: {},
      logout: jest.fn()
    };

    TestBed.configureTestingModule({ providers: [UserService,
      {
        provide: Keycloak,
        useValue: keycloakMock
      }] });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should return null if the token is undefined', () => {
    keycloakMock.tokenParsed = undefined;

    const result = service.getName();

    expect(result)
      .toBeNull();
  });

  it('should return "name" if it exists (Priority 1)', () => {
    keycloakMock.tokenParsed = {
      name: 'Super Admin',
      given_name: 'John',
      family_name: 'Doe'
    };

    const result = service.getName();

    expect(result)
      .toBe('Super Admin');
  });

  it('should combine given_name and family_name if "name" is missing (Priority 2)', () => {
    keycloakMock.tokenParsed = {
      name: undefined,
      given_name: 'Jane',
      family_name: 'Smith'
    };

    const result = service.getName();

    expect(result)
      .toBe('Jane Smith');
  });

  it('should return null if no name fields are present', () => {
    keycloakMock.tokenParsed = {
      email: 'test@example.com'
    };

    const result = service.getName();

    expect(result)
      .toBeNull();
  });

  it('should call keycloak logout', () => {
    service.logout();
    expect(keycloakMock.logout)
      .toHaveBeenCalled();
  });
});
