import { TestBed } from '@angular/core/testing';

import { OrganisationUnitService } from './organisation-unit.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { OrganisationUnitModel } from './organisation-unit.model';
import { organisationUnit1, organisationUnit2 } from '../../shared/test/test-data';

describe('OrganisationUnitService', () => {
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/organisation-units';
  let service: OrganisationUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganisationUnitService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(OrganisationUnitService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllMembers', () => {
    it('should call httpClient.get with the correct URL and return members', () => {
      const mockMembers: OrganisationUnitModel[] = [organisationUnit1,
        organisationUnit2];

      service.getAllOrganisationUnits()
        .subscribe((members) => {
          expect(members)
            .toEqual(mockMembers);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockMembers);
    });
  });
});
