import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDegreeComponent } from './add-degree.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { degree1 } from '../../../shared/test/test-data';
import { provideTranslateService } from '@ngx-translate/core';


describe('AddDegree', () => {
  let component: AddDegreeComponent;
  let fixture: ComponentFixture<AddDegreeComponent>;

  const dialogData = degree1;
  const dialogRefMock = { close: jest.fn() };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddDegreeComponent],
      providers: [{ provide: MAT_DIALOG_DATA,
        useValue: dialogData },
      { provide: MatDialogRef,
        useValue: dialogRefMock },

      provideTranslateService()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddDegreeComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
