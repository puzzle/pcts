import { TestBed } from '@angular/core/testing';

import { RoleService } from './role.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { RoleModel } from './RoleModel';
import { role1, role2 } from '../../shared/test/test-data';

describe('RoleService', () => {
  let service: RoleService;
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/roles';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RoleService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();
    service = TestBed.inject(RoleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    expect(service)
      .toBeTruthy();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllRoles', () => {
    it('should call httpClient.get with the correct URL and return roles', () => {
      const mockRoles: RoleModel[] = [role1,
        role2];

      service.getAllRoles()
        .subscribe((roles) => {
          expect(roles)
            .toEqual(mockRoles);
        });

      const req = httpMock.expectOne(`${API_URL}`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockRoles);
    });
  });
});
