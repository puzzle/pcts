import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { certificateOverviewResolver } from './certificate-overview-resolver';

describe('certificateResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => certificateOverviewResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
