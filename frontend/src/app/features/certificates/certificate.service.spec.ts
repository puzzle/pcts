import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { CertificateService } from './certificate.service';
import { provideHttpClient } from '@angular/common/http';
import { CertificateModel } from './certificate.model';


describe('OrganisationUnitService', () => {
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


  describe('getCertificateById', () => {
    it('should call httpClient.get with the correct URL and return organisationUnits', () => {
      const mockCertificate: CertificateModel[] = certificate;

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
  });
});
