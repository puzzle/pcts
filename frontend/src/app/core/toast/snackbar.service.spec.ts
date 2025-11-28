import { TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from './snackbar.component';
import { SnackbarService } from './snackbar.service';

describe('SnackbarService', () => {
  let service: SnackbarService;
  let snackBarMock: jest.Mocked<MatSnackBar>;

  beforeEach(() => {
    snackBarMock = {
      openFromComponent: jest.fn()
    } as any;

    TestBed.configureTestingModule({
      providers: [SnackbarService,
        { provide: MatSnackBar,
          useValue: snackBarMock }]
    });

    service = TestBed.inject(SnackbarService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should call openFromComponent with correct params for success toast', () => {
    const items: string[] = ['Hello'];

    service.showToasts(items, 'success');

    expect(snackBarMock.openFromComponent)
      .toHaveBeenCalledWith(SnackbarComponent, expect.objectContaining({
        data: items,
        panelClass: 'success',
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
        duration: 5000
      }));
  });

  it('should call openFromComponent with correct params for error toast', () => {
    const items: string[] = ['Oops!'];

    service.showToasts(items, 'error');

    expect(snackBarMock.openFromComponent)
      .toHaveBeenCalledWith(SnackbarComponent, expect.objectContaining({
        data: items,
        panelClass: 'error',
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
        duration: 5000
      }));
  });

  it('should pass the SnackbarComponent as the first argument of openFromComponent', () => {
    const items: string[] = [];

    service.showToasts(items, 'success');

    const [component] = snackBarMock.openFromComponent.mock.calls[0];
    expect(component)
      .toBe(SnackbarComponent);
  });
});
