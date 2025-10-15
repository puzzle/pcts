import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormFieldError } from './form-field-error';

describe('FormFieldError', () => {
  let component: FormFieldError;
  let fixture: ComponentFixture<FormFieldError>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [FormFieldError]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormFieldError);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
