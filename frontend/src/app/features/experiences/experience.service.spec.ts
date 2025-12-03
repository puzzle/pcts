import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { experience1, experience2 } from '../../shared/test/test-data';
import { ExperienceService } from './experience.service';
import { ExperienceModel } from './experience.model';

describe('experienceService', () => {
  let httpMock: HttpTestingController;
  let service: ExperienceService;
  const API_URL = '/api/v1/experiences';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExperienceService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(ExperienceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });


  describe('Experience CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the experience with the correct id', () => {
      const mockExperience: ExperienceModel = experience1;

      service.getExperienceById(1)
        .subscribe((experience) => {
          expect(experience)
            .toEqual(mockExperience);
        });

      const req = httpMock.expectOne(`${API_URL}/1`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockExperience);
    });
    it('should call httpClient.post with the correct URL and body and return the created experience', () => {
      const mockExperience: ExperienceModel = experience1;

      service.addExperience(experience1)
        .subscribe((experience) => {
          expect(experience)
            .toEqual(mockExperience);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      req.flush(mockExperience);
    });
    it('should call httpClient.put with the correct URL and body and return the updated experience', () => {
      const mockExperience: ExperienceModel = experience2;

      service.updateExperience(2, experience2)
        .subscribe((experience) => {
          expect(experience)
            .toEqual(mockExperience);
        });

      const req = httpMock.expectOne(`${API_URL}/2`);
      expect(req.request.method)
        .toBe('PUT');
      req.flush(mockExperience);
    });
    it('should call httpClient.delete with the correct URL', () => {
      service.deleteExperience(3)
        .subscribe();

      const req = httpMock.expectOne(`${API_URL}/3`);
      expect(req.request.method)
        .toBe('DELETE');
    });
  });
});
