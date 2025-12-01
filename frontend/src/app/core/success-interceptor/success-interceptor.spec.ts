import { TestBed } from '@angular/core/testing';
import { HttpBackend, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { successInterceptor } from './success-interceptor';
import { url } from '../../shared/test/test-data';

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

    http.get(url)
      .subscribe(() => {
        expect(toastService.showToasts).not.toHaveBeenCalled();
        done();
      });
  });


  it('should trigger toast on successful POST request', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.post(url, {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('POST', { OBJECT: 'Data' });
        expect(toastService.showToasts)
          .toHaveBeenCalledWith(['Translated Message'], 'success');
        done();
      });
  });


  it('should singularize and capitalize object names', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.put('/api/v1/organisations-companies', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('PUT', { OBJECT: 'Organisation Company' });
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
});
