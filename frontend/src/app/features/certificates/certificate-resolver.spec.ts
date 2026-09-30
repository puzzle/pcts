import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { certificateTypeResolver } from './certificate-type.resolver';

describe('certificateResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => certificateTypeResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
