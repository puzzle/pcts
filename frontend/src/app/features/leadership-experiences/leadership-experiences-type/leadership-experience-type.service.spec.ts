import { TestBed } from '@angular/core/testing';

import { LeadershipExperienceTypeService } from './leadership-experience-type.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LeadershipExperienceTypeModel } from './leadership-experience-type.model';
import {
  leadershipExperienceType1,
  leadershipExperienceType2
} from '../../../shared/test/test-data';

describe('LeadershipExperienceTypeService', () => {
  let service: LeadershipExperienceTypeService;
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/leadership-experience-types';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LeadershipExperienceTypeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(LeadershipExperienceTypeService);
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
    it('should call httpClient.get with the correct URL and return all the leadership-experience-types', () => {
      const mockLeadershipExperiencesType: LeadershipExperienceTypeModel[] = [leadershipExperienceType1,
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
