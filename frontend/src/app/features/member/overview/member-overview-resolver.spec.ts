import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';

import { memberOverviewResolver } from './member-overview-resolver';

describe('memberOverviewResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => memberOverviewResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
