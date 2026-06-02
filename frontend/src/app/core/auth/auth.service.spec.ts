import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import Keycloak from 'keycloak-js';
import { APP_CONFIG } from '../../features/configuration/configuration.token';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEvent, KeycloakEventType } from 'keycloak-angular';
import { signal, WritableSignal } from '@angular/core';

type MockKeycloak = Partial<Keycloak> & { tokenParsed?: any };

describe('AuthService', () => {
  let service: AuthService;
  let keycloakMock: MockKeycloak;
  let mockEventSignal: WritableSignal<KeycloakEvent>;
  const appConfigMock = {
    adminAuthorities: ['ADMIN_ROLE',
      'HR_ROLE']
  };

  beforeEach(() => {
    mockEventSignal = signal({ type: KeycloakEventType.AuthRefreshSuccess,
      args: false });

    keycloakMock = {
      tokenParsed: {},
      logout: jest.fn()
    };

    TestBed.configureTestingModule({ providers: [
      AuthService,
      {
        provide: Keycloak,
        useValue: keycloakMock
      },
      {
        provide: APP_CONFIG,
        useValue: appConfigMock
      },
      {
        provide: KEYCLOAK_EVENT_SIGNAL,
        useValue: mockEventSignal
      }
    ] });
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('name signal', () => {
    it('should return null if the token is undefined', () => {
      keycloakMock.tokenParsed = undefined;
      mockEventSignal.set({ type: KeycloakEventType.AuthSuccess,
        args: true });

      const result = service.name();

      expect(result)
        .toBeNull();
    });

    it('should return "name" if it exists', () => {
      keycloakMock.tokenParsed = {
        name: 'Super Admin',
        given_name: 'John',
        family_name: 'Doe'
      };
      mockEventSignal.set({ type: KeycloakEventType.AuthRefreshSuccess,
        args: true });

      const result = service.name();

      expect(result)
        .toBe('Super Admin');
    });

    it('should combine given_name and family_name if "name" is missing', () => {
      keycloakMock.tokenParsed = {
        name: undefined,
        given_name: 'Jane',
        family_name: 'Smith'
      };
      mockEventSignal.set({ type: KeycloakEventType.AuthRefreshSuccess,
        args: true });

      const result = service.name();

      expect(result)
        .toBe('Jane Smith');
    });

    it('should return null if no name fields are present', () => {
      keycloakMock.tokenParsed = {
        email: 'test@example.com'
      };
      mockEventSignal.set({ type: KeycloakEventType.AuthRefreshSuccess,
        args: true });

      const result = service.name();

      expect(result)
        .toBeNull();
    });
  });

  it('should call keycloak logout', () => {
    service.logout();
    expect(keycloakMock.logout)
      .toHaveBeenCalled();
  });
});
