import { TestBed } from '@angular/core/testing';

import { ConfigurationService } from './configuration.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ConfigurationModel } from './configuration.model';
import { configuration, helpUrl } from '../../shared/test/test-data';
import { HelpUrlModel } from './HelpUrl.model';

describe('configurationService', () => {
  let httpMock: HttpTestingController;
  let service: ConfigurationService;
  const API_URL = '/api/v1/configuration/';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(),
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

      const req = httpMock.expectOne(API_URL + 'authorization');
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockConfig);
    });
  });

  describe('Supportpageurl endpoint', () => {
    it('should fetch url', () => {
      const mockUrl: HelpUrlModel = helpUrl;

      service.getHelpUrl()
        .subscribe((response) => {
          expect(response)
            .toEqual(mockUrl);
        });

      const req = httpMock.expectOne(API_URL + 'help');
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockUrl);
    });
  });
});
