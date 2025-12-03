import { TestBed } from '@angular/core/testing';

import { LeadershipExperiencesTypeService } from './leadership-experiences-type.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LeadershipExperiencesTypeModel } from './leadership-experiences-type.model';
import {
  leadershipExperienceType1,
  leadershipExperienceType2
} from '../../../shared/test/test-data';

describe('LeadershipExperiencesTypeService', () => {
  let service: LeadershipExperiencesTypeService;
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/leadership-experiences-types';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LeadershipExperiencesTypeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(LeadershipExperiencesTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('LeadershipExperiencesType CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the experience with the correct id', () => {
      const mockLeadershipExperiencesType: LeadershipExperiencesTypeModel[] = [leadershipExperienceType1,
        leadershipExperienceType2];

      service.getAllLeadershipTypes()
        .subscribe((leadershipExperiencesTypes) => {
          expect(leadershipExperiencesTypes)
            .toEqual(mockLeadershipExperiencesType);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockLeadershipExperiencesType);
    });
  });
});
