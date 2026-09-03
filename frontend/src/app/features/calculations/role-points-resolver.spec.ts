import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { rolePointsResolver } from './role-points-resolver';

describe('rolePointsResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => rolePointsResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
