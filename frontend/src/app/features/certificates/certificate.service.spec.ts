import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { CertificateService } from './certificate.service';
import { provideHttpClient } from '@angular/common/http';
import { CertificateModel } from './certificate.model';
import { certificate1, certificate2 } from '../../shared/test/test-data';


describe('certificateService', () => {
  let httpMock: HttpTestingController;
  let service: CertificateService;
  const API_URL = '/api/v1/certificates';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CertificateService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(CertificateService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });


  describe('Certificate CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the certificate with the correct id', () => {
      const mockCertificate: CertificateModel = certificate1;

      service.getCertificateById(1)
        .subscribe((certificate) => {
          expect(certificate)
            .toEqual(mockCertificate);
        });

      const req = httpMock.expectOne(`${API_URL}/1`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockCertificate);
    });
    it('should call httpClient.post with the correct URL and body and return the created certificate', () => {
      const mockCertificate: CertificateModel = certificate1;

      service.addCertificate(certificate1)
        .subscribe((certificate) => {
          expect(certificate)
            .toEqual(mockCertificate);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      req.flush(mockCertificate);
    });
    it('should call httpClient.put with the correct URL and body and return the updated certificate', () => {
      const mockCertificate: CertificateModel = certificate2;

      service.updateCertificate(2, certificate2)
        .subscribe((certificate) => {
          expect(certificate)
            .toEqual(mockCertificate);
        });

      const req = httpMock.expectOne(`${API_URL}/2`);
      expect(req.request.method)
        .toBe('PUT');
      req.flush(mockCertificate);
    });
    it('should call httpClient.delete with the correct URL', () => {
      service.deleteCertificate(3)
        .subscribe();

      const req = httpMock.expectOne(`${API_URL}/3`);
      expect(req.request.method)
        .toBe('DELETE');
    });
  });
});
