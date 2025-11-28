import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SnackbarComponent } from './snackbar.component';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

describe('SnackbarComponent', () => {
  let component: SnackbarComponent;
  let fixture: ComponentFixture<SnackbarComponent>;
  let snackBarRefMock: { dismiss: jest.Mock };

  const testMessages: string[] = ['Message 1',
    'Message 2',
    'Message 3'];

  beforeEach(async() => {
    snackBarRefMock = { dismiss: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [MatIconModule,
        MatButtonModule,
        SnackbarComponent],
      providers: [{ provide: MatSnackBarRef,
        useValue: snackBarRefMock },
      { provide: MAT_SNACK_BAR_DATA,
        useValue: testMessages }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should initialize messages from MAT_SNACK_BAR_DATA', () => {
    expect(component.messages())
      .toEqual(testMessages);
  });

  it('should remove a message by index', () => {
    component.close(1);
    expect(component.messages())
      .toEqual(['Message 1',
        'Message 3']);
    expect(snackBarRefMock.dismiss).not.toHaveBeenCalled();
  });

  it('should dismiss snackbar when last message is removed', () => {
    component.close(0);
    component.close(0);
    component.close(0);
    expect(component.messages())
      .toEqual([]);
    expect(snackBarRefMock.dismiss)
      .toHaveBeenCalledTimes(1);
  });
});
