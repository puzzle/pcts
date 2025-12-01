import { TestBed } from '@angular/core/testing';
import { HttpBackend, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { throwError } from 'rxjs';

import { SnackbarService } from '../toast/snackbar.service';
import { ScopedTranslationService } from '../../shared/services/scoped-translation.service';
import { errorInterceptor } from './error-interceptor';
import { url } from '../../shared/test/test-data';

describe('errorInterceptor', () => {
  let http: HttpClient;
  let toastService: jest.Mocked<SnackbarService>;
  let translate: jest.Mocked<ScopedTranslationService>;
  let backend: jest.Mocked<HttpBackend>;

  beforeEach(() => {
    toastService = { showToasts: jest.fn() } as any;

    translate = {
      instant: jest.fn()
        .mockImplementation((key: string) => key)
    } as any;

    backend = { handle: jest.fn() } as any;

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([errorInterceptor])),
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

  it('should trigger default error toast when error is not array', (done) => {
    translate.instant.mockReturnValueOnce('Translated default');

    const error = new HttpErrorResponse({
      status: 500,
      error: 'server-down'
    });

    backend.handle.mockReturnValue(throwError(() => error));

    http.get(url)
      .subscribe({
        error: () => {
          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.DEFAULT');
          expect(toastService.showToasts)
            .toHaveBeenCalledWith(['Translated default'], 'error');
          done();
        }
      });
  });

  it('should show translated messages for array errors', (done) => {
    const backendError = new HttpErrorResponse({
      status: 400,
      error: [{ key: 'INVALID_NAME',
        values: { field: 'Name' } },
      { key: 'TOO_LONG',
        values: { max: 10 } }]
    });

    translate.instant
      .mockReturnValueOnce('Translated invalid name')
      .mockReturnValueOnce('Translated too long');

    backend.handle.mockReturnValue(throwError(() => backendError));

    http.post(url, {})
      .subscribe({
        error: () => {
          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.INVALID_NAME', { field: 'Name' });
          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.TOO_LONG', { max: 10 });

          expect(toastService.showToasts)
            .toHaveBeenCalledWith(['Translated invalid name',
              'Translated too long'], 'error');
          done();
        }
      });
  });

  it('should fallback to default message if translation returns unchanged key', (done) => {
    const backendError = new HttpErrorResponse({
      status: 400,
      error: [{ key: 'UNKNOWN',
        values: {} }]
    });

    translate.instant
      .mockReturnValueOnce('ERROR.UNKNOWN')
      .mockReturnValueOnce('Fallback message');

    backend.handle.mockReturnValue(throwError(() => backendError));

    http.put(url, {})
      .subscribe({
        error: () => {
          expect(toastService.showToasts)
            .toHaveBeenCalledWith(['Fallback message'], 'error');
          done();
        }
      });
  });

  it('should rethrow the error after handling', (done) => {
    const backendError = new HttpErrorResponse({
      status: 400,
      error: 'oops'
    });

    translate.instant.mockReturnValue('Translated default');
    backend.handle.mockReturnValue(throwError(() => backendError));

    http.get(url)
      .subscribe({
        next: () => {
          fail('Expected error');
        },
        error: (err) => {
          expect(err)
            .toBe(backendError);
          done();
        }
      });
  });

  it('should handle array items without values', (done) => {
    const backendError = new HttpErrorResponse({
      status: 422,
      error: [{ key: 'MISSING_FIELD' }]
    });

    translate.instant.mockReturnValueOnce('Missing field');

    backend.handle.mockReturnValue(throwError(() => backendError));

    http.post(url, {})
      .subscribe({
        error: () => {
          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.MISSING_FIELD', {});
          expect(toastService.showToasts)
            .toHaveBeenCalledWith(['Missing field'], 'error');
          done();
        }
      });
  });

  it('should truncate values.IS if it exceeds 15 characters', (done) => {
    const longString = 'ThisIsAVeryLongStringThatNeedsTruncating';
    const backendError = new HttpErrorResponse({
      status: 400,
      error: [{ key: 'INVALID_VALUE',
        values: { IS: longString } }]
    });

    translate.instant.mockReturnValue('Translated Message');
    backend.handle.mockReturnValue(throwError(() => backendError));

    http.post(url, {})
      .subscribe({
        error: () => {
          const expectedTruncation = 'ThisIsAVeryLong...';

          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.INVALID_VALUE', expect.objectContaining({
              IS: expectedTruncation
            }));
          done();
        }
      });
  });

  it('should translate FIELD and CLASS params with screaming snake case keys', (done) => {
    const backendError = new HttpErrorResponse({
      status: 400,
      error: [{
        key: 'REQUIRED',
        values: { FIELD: 'firstName',
          CLASS: 'userProfile' }
      }]
    });

    translate.instant.mockImplementation((key: string) => {
      switch (key) {
        case 'USER_PROFILE.MODEL_NAME':
          return 'User Profile Model';
        case 'USER_PROFILE.FIRST_NAME':
          return 'First Name Label';
        case 'ERROR.REQUIRED':
          return 'Final Error Message';
        default:
          return key;
      }
    });

    backend.handle.mockReturnValue(throwError(() => backendError));

    http.post(url, {})
      .subscribe({
        error: () => {
          expect(translate.instant)
            .toHaveBeenCalledWith('USER_PROFILE.FIRST_NAME');

          expect(translate.instant)
            .toHaveBeenCalledWith('USER_PROFILE.MODEL_NAME');

          expect(translate.instant)
            .toHaveBeenCalledWith('ERROR.REQUIRED', {
              FIELD: 'First Name Label',
              CLASS: 'User Profile Model'
            });

          expect(toastService.showToasts)
            .toHaveBeenCalledWith(['Final Error Message'], 'error');
          done();
        }
      });
  });
});
