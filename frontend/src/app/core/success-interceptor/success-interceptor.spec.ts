import { TestBed } from '@angular/core/testing';
import { HttpBackend, HttpClient, HttpResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
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
        .mockImplementation((key) => `Translated: ${key}`)
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

    http.post('/api/v1/data-items', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('POST', {
            OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
          });
        expect(toastService.showToasts)
          .toHaveBeenCalledWith(['Translated: POST'], 'success');
        done();
      });
  });

  it('should trigger toast on successful DELETE request', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.delete('/api/v1/data-items', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('DELETE', {
            OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
          });
        expect(toastService.showToasts)
          .toHaveBeenCalledWith(['Translated: DELETE'], 'success');
        done();
      });
  });

  it('should trigger toast on successful PUT request', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.put('/api/v1/data-items', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('PUT', {
            OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
          });
        expect(toastService.showToasts)
          .toHaveBeenCalledWith(['Translated: PUT'], 'success');
        done();
      });
  });

  it('should singularize and join multi-part object names', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.put('/api/v1/organisations-companies', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('PUT', {
            OBJECT: 'Translated: ORGANISATION_COMPANY.MODEL_NAME'
          });
        done();
      });
  });

  it('should fallback to "Object" if URL does not have enough segments', (done) => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    http.post('/api', {})
      .subscribe(() => {
        expect(translate.instant)
          .toHaveBeenCalledWith('POST', {
            OBJECT: 'Translated: OBJECT.MODEL_NAME'
          });
        done();
      });
  });
});
