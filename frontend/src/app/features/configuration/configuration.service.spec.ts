import { TestBed } from '@angular/core/testing';

import { ConfigurationService } from './configuration.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DegreeService } from '../degrees/degree.service';
import { provideHttpClient } from '@angular/common/http';
import { ConfigurationModel } from './configuration.model';
import { configuration } from '../../shared/test/test-data';

describe('configurationService', () => {
  let httpMock: HttpTestingController;
  let service: ConfigurationService;
  const API_URL = '/api/v1/configuration';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DegreeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(ConfigurationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('Configuration endpoint', () => {
    it('should fetch configuration', () => {
      const mockConfig: ConfigurationModel = configuration;

      service.getConfiguration()
        .subscribe((config) => {
          expect(config)
            .toEqual(mockConfig);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockConfig);
    });
  });
});
