import { TestBed } from '@angular/core/testing';

import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { CertificateTypeService } from './certificate-type.service';
import { CertificateTypeModel } from './certificate-type.model';
import { certificateType1, certificateType2 } from '../../../shared/test/test-data';

describe('CertificateTypeService', () => {
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/organisation-units';
  let service: CertificateTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CertificateTypeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(CertificateTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllCertificateTypes', () => {
    it('should call httpClient.get with the correct URL and return all certificateTypes', () => {
      const mockCertificateTypes: CertificateTypeModel[] = [certificateType1,
        certificateType2];

      service.getAllCertificateTypes()
        .subscribe((certificateTypes) => {
          expect(certificateTypes)
            .toEqual(mockCertificateTypes);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockCertificateTypes);
    });
  });
});
