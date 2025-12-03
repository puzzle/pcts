import { TestBed } from '@angular/core/testing';

import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ExperienceTypeService } from './experience-type.service';
import { ExperienceTypeModel } from './experience-type.model';
import { experienceType1, experienceType2 } from '../../../shared/test/test-data';

describe('ExperienceTypeService', () => {
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/experience-types';
  let service: ExperienceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExperienceTypeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(ExperienceTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllExperienceTypes', () => {
    it('should call httpClient.get with the correct URL and return all experienceTypes', () => {
      const mockExperienceTypes: ExperienceTypeModel[] = [experienceType1,
        experienceType2];

      service.getAllExperienceTypes()
        .subscribe((experienceTypes) => {
          expect(experienceTypes)
            .toEqual(mockExperienceTypes);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockExperienceTypes);
    });
  });
});
