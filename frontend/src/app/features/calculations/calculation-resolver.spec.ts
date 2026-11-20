import { TestBed } from '@angular/core/testing';
import { ResolveFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { calculationResolver } from './calculation-resolver';
import { CalculationService } from './calculation.service';
import { CalculationModel } from './calculationDto';
import { calculation1 } from '../../shared/test/test-data';

describe('calculationResolver', () => {
  const executeResolver: ResolveFn<CalculationModel> = (...resolverParameters) => TestBed.runInInjectionContext(() => calculationResolver(...resolverParameters));

  let mockRoute: ActivatedRouteSnapshot;
  let mockState: RouterStateSnapshot;
  let mockCalculationService: jest.Mocked<CalculationService>;

  beforeEach(() => {
    mockCalculationService = {
      getCalculationById: jest.fn()
    } as unknown as jest.Mocked<CalculationService>;

    TestBed.configureTestingModule({
      providers: [{ provide: CalculationService,
        useValue: mockCalculationService }]
    });

    mockRoute = {
      paramMap: {
        get: jest.fn()
      }
    } as unknown as ActivatedRouteSnapshot;

    mockState = {} as RouterStateSnapshot;
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });

  it('should return the Calculation', () => {
    const id = 1;
    (mockRoute.paramMap.get as jest.Mock).mockReturnValue(id.toString());

    mockCalculationService.getCalculationById.mockReturnValue(calculation1 as any);

    const result = executeResolver(mockRoute, mockState);

    expect(mockRoute.paramMap.get)
      .toHaveBeenCalledWith('calculationId');
    expect(mockCalculationService.getCalculationById)
      .toHaveBeenCalledWith(id);
    expect(result)
      .toEqual(calculation1);
  });

  it('should throw an error if id is missing', () => {
    (mockRoute.paramMap.get as jest.Mock).mockReturnValue(null);

    expect(() => executeResolver(mockRoute, mockState))
      .toThrow('calculation id is missing in the route');
  });
});
