import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { memberIdResolver } from './member-id-resolver';

describe('memberIdResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => memberIdResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
