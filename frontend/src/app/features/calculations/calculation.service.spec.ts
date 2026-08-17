import { TestBed } from '@angular/core/testing';

import { CalculationService } from './calculation.service.ts';

describe('CalculationService', () => {
  let service: CalculationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalculationService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
