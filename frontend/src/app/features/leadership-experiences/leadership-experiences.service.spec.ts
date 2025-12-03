import { TestBed } from '@angular/core/testing';

import { LeadershipExperiencesService } from './leadership-experiences.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LeadershipExperiencesModel } from './leadership-experiences.model';
import { leadershipExperience1 } from '../../shared/test/test-data';

describe('LeadershipExperiencesService', () => {
  let httpMock: HttpTestingController;
  let service: LeadershipExperiencesService;
  const API_URL = '/api/v1/leadership-experiences';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LeadershipExperiencesService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();
    service = TestBed.inject(LeadershipExperiencesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('LeadershipExperiences CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the experience with the correct id', () => {
      const mockLeadershipExperiences: LeadershipExperiencesModel = leadershipExperience1;

      service.getLeadershipExperienceById(1)
        .subscribe((leadershipExperience) => {
          expect(leadershipExperience)
            .toEqual(mockLeadershipExperiences);
        });

      const req = httpMock.expectOne(`${API_URL}/1`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockLeadershipExperiences);
    });

    it('should call httpClient.post with the correct URL and body and return the created leadership Experience', () => {
      const mockLeadershipExperiences: LeadershipExperiencesModel = leadershipExperience1;

      service.addLeadershipExperience(leadershipExperience1)
        .subscribe((leadershipExperience) => {
          expect(leadershipExperience)
            .toEqual(mockLeadershipExperiences);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      req.flush(mockLeadershipExperiences);
    });

    it('should call httpClient.put with the correct URL and body and return the updated leadership Experience', () => {
      const mockLeadershipExperiences: LeadershipExperiencesModel = leadershipExperience1;

      service.updateLeadershipExperience(2, leadershipExperience1)
        .subscribe((leadershipExperience) => {
          expect(leadershipExperience)
            .toEqual(mockLeadershipExperiences);
        });

      const req = httpMock.expectOne(`${API_URL}/2`);
      expect(req.request.method)
        .toBe('PUT');
      req.flush(mockLeadershipExperiences);
    });

    it('should call httpClient.delete with the correct URL', () => {
      service.deleteLeadershipExperience(3)
        .subscribe();

      const req = httpMock.expectOne(`${API_URL}/3`);
      expect(req.request.method)
        .toBe('DELETE');
    });
  });
});
