import { TestBed } from '@angular/core/testing';
import { HttpBackend, HttpClient, HttpResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
import { lastValueFrom, of } from 'rxjs';

import { SnackbarService } from '../toast/snackbar.service';
import { successInterceptor } from './success-interceptor';
import { ScopedTranslationService } from '../../shared/i18n-prefix.provider';

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
        {
          provide: HttpBackend,
          useValue: backend
        },
        {
          provide: SnackbarService,
          useValue: toastService
        },
        {
          provide: ScopedTranslationService,
          useValue: translate
        }
      ]
    });

    http = TestBed.inject(HttpClient);
  });

  it('should NOT trigger toast on GET requests', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.get('/api/v1/users'));

    expect(toastService.showToasts).not.toHaveBeenCalled();
  });

  it('should trigger toast on successful POST request', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.post('/api/v1/data-items', {}));

    expect(translate.instant)
      .toHaveBeenCalledWith('POST', {
        OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
      });
    expect(toastService.showToasts)
      .toHaveBeenCalledWith(['Translated: POST'], 'success');
  });

  it('should trigger toast on successful DELETE request', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.delete('/api/v1/data-items', {}));

    expect(translate.instant)
      .toHaveBeenCalledWith('DELETE', {
        OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
      });
    expect(toastService.showToasts)
      .toHaveBeenCalledWith(['Translated: DELETE'], 'success');
  });

  it('should trigger toast on successful PUT request', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.put('/api/v1/data-items', {}));

    expect(translate.instant)
      .toHaveBeenCalledWith('PUT', {
        OBJECT: 'Translated: DATA_ITEM.MODEL_NAME'
      });
    expect(toastService.showToasts)
      .toHaveBeenCalledWith(['Translated: PUT'], 'success');
  });

  it('should singularize and join multi-part object names', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.put('/api/v1/organisations-companies', {}));

    expect(translate.instant)
      .toHaveBeenCalledWith('PUT', {
        OBJECT: 'Translated: ORGANISATION_COMPANY.MODEL_NAME'
      });
  });

  it('should fallback to "Object" if URL does not have enough segments', async() => {
    backend.handle.mockReturnValue(of(new HttpResponse({ status: 200 })));

    await lastValueFrom(http.post('/api', {}));

    expect(translate.instant)
      .toHaveBeenCalledWith('POST', {
        OBJECT: 'Translated: OBJECT.MODEL_NAME'
      });
  });
});
