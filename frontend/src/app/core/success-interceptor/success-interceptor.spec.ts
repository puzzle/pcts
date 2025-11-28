import { TestBed } from '@angular/core/testing';
import { HttpBackend, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { successInterceptor } from './success-interceptor';

describe('successInterceptor', () => {
  let http: HttpClient;
  let toastService: jest.Mocked<SnackbarService>;
  let translate: jest.Mocked<ScopedTranslationService>;
  let backend: jest.Mocked<HttpBackend>;

  beforeEach(() => {
    toastService = { showToasts: jest.fn() } as any;

    translate = {
      instant: jest.fn()
        .mockReturnValue('Translated Message')
    } as any;

    backend = { handle: jest.fn() } as any;

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([successInterceptor])),
        { provide: HttpBackend,
          useValue: backend },
        { provide: SnackbarService,
          useValue: toastService },
        { provide: ScopedTranslationService,
          useValue: translate }
      ]
    });

    http = TestBed.inject(HttpClient);
  });

  it('should NOT trigger toast on GET requests', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.get('/api/v1/users')
      .subscribe(() => {
        expect(toastService.showToasts).not.toHaveBeenCalled();
        done();
      });
  });


  it('should trigger toast on successful POST request', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.post('/api/v1/users', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('POST', { OBJECT: 'User' });
        expect(toastService.showToasts)
          .toHaveBeenCalledWith(['Translated Message'], 'success');
        done();
      });
  });


  it('should singularize plural object names', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.put('/api/v1/companies', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('PUT', { OBJECT: 'Company' });
        done();
      });
  });


  it('should use fallback "Object" if URL does not contain enough segments', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.post('/api', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('POST', { OBJECT: 'Object' });
        done();
      });
  });


  it('should capitalize object names', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.patch('/api/v1/orders', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('PATCH', { OBJECT: 'Order' });
        done();
      });
  });
});
