import { TestBed } from '@angular/core/testing';
import { CalculationService } from './calculation.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { calculationModel1 } from '../../shared/test/test-data';
import { CalculationModel } from './calculationDto';

describe('CalculationService', () => {
  let httpMock: HttpTestingController;
  let service: CalculationService;
  const API_URL = '/api/v1/calculations';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CalculationService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(CalculationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('Calculation CRUD endpoints', () => {
    it('should call httpClient.get with the correct URL and return the Calculation with the correct id', () => {
      const mockCalculation: CalculationModel = calculationModel1;

      service.getCalculationById(1)
        .subscribe((calc) => {
          expect(calc)
            .toEqual(mockCalculation);
        });

      const req = httpMock.expectOne(`${API_URL}/1`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockCalculation);
    });
    it('should call httpClient.post with the correct URL and body and return the created Calculation', () => {
      const mockCalculation: CalculationModel = calculationModel1;

      service.addCalculation(calculationModel1)
        .subscribe((calc) => {
          expect(calc)
            .toEqual(mockCalculation);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      req.flush(mockCalculation);
    });
    it('should call httpClient.put with the correct URL and body and return the updated Calculation', () => {
      const mockCalculation: CalculationModel = calculationModel1;

      service.updateCalculation(2, calculationModel1)
        .subscribe((calc) => {
          expect(calc)
            .toEqual(mockCalculation);
        });
      const req = httpMock.expectOne(`${API_URL}/2`);
      expect(req.request.method)
        .toBe('PUT');
      req.flush(mockCalculation);
    });
  });
});
