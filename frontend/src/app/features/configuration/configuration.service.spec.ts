import { TestBed } from '@angular/core/testing';

import { ConfigurationService } from './configuration.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ConfigurationModel } from './configuration.model';
import { configuration, helpUrl } from '../../shared/test/test-data';
import { HelpUrlModel } from './HelpUrl.model';
import { firstValueFrom } from 'rxjs';

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

      const config = service.getConfiguration<configuration>();

      const configPromise = firstValueFrom(config);

      const req = httpMock.expectOne(API_URL + 'authorization');

      expect(req.request.method)
        .toBe('GET');

      req.flush(mockConfig);

      expect(await configPromise)
        .toEqual(mockConfig);

      httpMock.verify();
    });
  });

  describe('Supportpageurl endpoint', () => {
    it('should fetch url', async() => {
      const mockUrl: HelpUrlModel = helpUrl;

      const url = service.getHelpUrl<helpUrl>();

      const urlPromise = firstValueFrom(url);

      const req = httpMock.expectOne(API_URL + 'help');

      expect(req.request.method)
        .toBe('GET');

      req.flush(mockUrl);

      expect(await urlPromise)
        .toEqual(mockUrl);

      httpMock.verify();
    });
  });
});
