import { TestBed } from '@angular/core/testing';

import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { DegreeTypeService } from './degree-type.service';
import { DegreeTypeModel } from './degree-type.model';
import { degreeType1, degreeType2 } from '../../../shared/test/test-data';

describe('DegreeTypeService', () => {
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/degree-types';
  let service: DegreeTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DegreeTypeService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(DegreeTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllDegreeTypes', () => {
    it('should call httpClient.get with the correct URL and return all degreeTypes', () => {
      const mockDegreeTypes: DegreeTypeModel[] = [degreeType1,
        degreeType2];

      service.getAllDegreeTypes()
        .subscribe((degreeTypes) => {
          expect(degreeTypes)
            .toEqual(mockDegreeTypes);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockDegreeTypes);
    });
  });
});
