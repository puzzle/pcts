import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormFieldLabel } from './form-field-label';

describe('FormFieldLabel', () => {
  let component: FormFieldLabel;
  let fixture: ComponentFixture<FormFieldLabel>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [FormFieldLabel]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormFieldLabel);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
