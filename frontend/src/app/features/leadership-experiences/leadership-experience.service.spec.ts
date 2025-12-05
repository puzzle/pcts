import { TestBed } from '@angular/core/testing';

import { LeadershipExperienceService } from './leadership-experience.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LeadershipExperienceModel } from './leadership-experience.model';
import { leadershipExperience1, leadershipExperience2 } from '../../shared/test/test-data';

describe('LeadershipExperienceService', () => {
  let httpMock: HttpTestingController;
  let service: LeadershipExperienceService;
  const API_URL = '/api/v1/leadership-experiences';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LeadershipExperienceService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();
    service = TestBed.inject(LeadershipExperienceService);
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
    it('should call httpClient.get with the correct URL and return the leadership experience with the correct id', () => {
      const mockLeadershipExperiences: LeadershipExperienceModel = leadershipExperience1;

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

    it('should call httpClient.post with the correct URL and body and return the created leadership experience', () => {
      const mockLeadershipExperiences: LeadershipExperienceModel = leadershipExperience1;

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

    it('should call httpClient.put with the correct URL and body and return the updated leadership experience', () => {
      const mockLeadershipExperiences: LeadershipExperienceModel = leadershipExperience2;

      service.updateLeadershipExperience(2, leadershipExperience2)
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
