import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { calculationTemplateResolver } from './calculation-template-resolver'; // Update with your actual file name
import { CalculationService } from './calculation.service';
import { CalculationModel } from './calculationDto';
import { calculation1 } from '../../shared/test/test-data';

describe('calculationTemplateResolver', () => {
  const executeResolver: ResolveFn<CalculationModel> = (...resolverParameters) => TestBed.runInInjectionContext(() => calculationTemplateResolver(...resolverParameters));

  let mockRoute: ActivatedRouteSnapshot;
  let mockState: RouterStateSnapshot;
  let mockCalculationService: jest.Mocked<CalculationService>;

  beforeEach(() => {
    mockCalculationService = {
      getTemplate: jest.fn()
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

  it('should return the Calculation template', () => {
    const memberId = 1;
    const roleId = 2;

    (mockRoute.paramMap.get as jest.Mock).mockImplementation((key: string) => {
      if (key === 'id') {
        return memberId.toString();
      }
      if (key === 'roleId') {
        return roleId.toString();
      }
      return null;
    });

    mockCalculationService.getTemplate.mockReturnValue(calculation1 as any);

    const result = executeResolver(mockRoute, mockState);

    expect(mockRoute.paramMap.get)
      .toHaveBeenCalledWith('id');
    expect(mockRoute.paramMap.get)
      .toHaveBeenCalledWith('roleId');
    expect(mockCalculationService.getTemplate)
      .toHaveBeenCalledWith(memberId, roleId);
    expect(result)
      .toEqual(calculation1);
  });

  it('should throw an error if member id is missing', () => {
    (mockRoute.paramMap.get as jest.Mock).mockReturnValue(null);

    expect(() => executeResolver(mockRoute, mockState))
      .toThrow('member id is missing in the route');
  });
});
