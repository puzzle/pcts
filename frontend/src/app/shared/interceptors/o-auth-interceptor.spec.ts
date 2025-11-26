import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn } from '@angular/common/http';

import { oAuthInterceptor } from './o-auth-interceptor';

describe('oAuthInterceptor', () => {
  const interceptor: HttpInterceptorFn = (req, next) => TestBed.runInInjectionContext(() => oAuthInterceptor(req, next));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(interceptor)
      .toBeTruthy();
  });
});
