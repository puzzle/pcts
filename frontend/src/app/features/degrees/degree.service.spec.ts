import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { degree1, degree2 } from '../../shared/test/test-data';
import { DegreeService } from './degree.service';
import { DegreeModel } from './degree.model';

describe('degreeService', () => {
  let httpMock: HttpTestingController;
  let service: DegreeService;
  const API_URL = '/api/v1/degrees';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DegreeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(DegreeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });


  describe('Degree CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the degree with the correct id', () => {
      const mockDegree: DegreeModel = degree1;

      service.getDegreeById(1)
        .subscribe((degree) => {
          expect(degree)
            .toEqual(mockDegree);
        });

      const req = httpMock.expectOne(`${API_URL}/1`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockDegree);
    });
    it('should call httpClient.post with the correct URL and body and return the created degree', () => {
      const mockDegree: DegreeModel = degree1;

      service.addDegree(degree1)
        .subscribe((degree) => {
          expect(degree)
            .toEqual(mockDegree);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      req.flush(mockDegree);
    });
    it('should call httpClient.put with the correct URL and body and return the updated degree', () => {
      const mockDegree: DegreeModel = degree2;

      service.updateDegree(2, degree2)
        .subscribe((degree) => {
          expect(degree)
            .toEqual(mockDegree);
        });

      const req = httpMock.expectOne(`${API_URL}/2`);
      expect(req.request.method)
        .toBe('PUT');
      req.flush(mockDegree);
    });
    it('should call httpClient.delete with the correct URL', () => {
      service.deleteDegree(3)
        .subscribe();

      const req = httpMock.expectOne(`${API_URL}/3`);
      expect(req.request.method)
        .toBe('DELETE');
    });
  });
});
