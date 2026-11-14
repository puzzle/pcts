import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { calculationTemplateResolver } from './calculation-template-resolver';

describe('calculationTemplateResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) => TestBed.runInInjectionContext(() => calculationTemplateResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver)
      .toBeTruthy();
  });
});
