import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { calculationResolver } from './calculation-resolver';

describe('calculationResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => calculationResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
