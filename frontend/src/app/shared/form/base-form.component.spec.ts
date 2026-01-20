import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BaseFormComponent } from './base-form.component';
import { provideTranslateService } from '@ngx-translate/core';

describe('BaseFormComponent', () => {
  let component: BaseFormComponent;
  let fixture: ComponentFixture<BaseFormComponent>;


  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [BaseFormComponent],
      providers: [provideTranslateService()]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseFormComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
  });

  it('should create the component', () => {
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should mark form as invalid if required fields are missing', () => {
    const baseFixture = TestBed.createComponent(BaseFormComponent);
    const form = new FormGroup({
      name: new FormControl('', Validators.required),
      lastName: new FormControl('Doe')
    });
    baseFixture.componentRef.setInput('formGroup', form);
    baseFixture.componentRef.setInput('formName', 'userForm');
    baseFixture.detectChanges();

    expect(form.valid)
      .toBe(false);
  });

  it('should mark form as valid if all required fields are filled', () => {
    const baseFixture = TestBed.createComponent(BaseFormComponent);
    const form = new FormGroup({
      name: new FormControl('John', Validators.required),
      lastName: new FormControl('Doe')
    });
    baseFixture.componentRef.setInput('formGroup', form);
    baseFixture.componentRef.setInput('formName', 'userForm');
    baseFixture.detectChanges();

    expect(form.valid)
      .toBe(true);
  });

  it('should emit submitted event when called', () => {
    const baseFixture = TestBed.createComponent(BaseFormComponent);
    const form = new FormGroup({
      name: new FormControl('John')
    });
    baseFixture.componentRef.setInput('formGroup', form);
    baseFixture.componentRef.setInput('formName', 'userForm');
    baseFixture.detectChanges();

    jest.spyOn(component.submitted, 'emit');
    component.submitted.emit();

    expect(component.submitted.emit)
      .toHaveBeenCalledTimes(1);
  });

  it('should emit canceled event when called', () => {
    const baseFixture = TestBed.createComponent(BaseFormComponent);
    const form = new FormGroup({
      name: new FormControl('John')
    });
    baseFixture.componentRef.setInput('formGroup', form);
    baseFixture.componentRef.setInput('formName', 'userForm');
    baseFixture.detectChanges();

    jest.spyOn(component.canceled, 'emit');
    component.canceled.emit();

    expect(component.canceled.emit)
      .toHaveBeenCalledTimes(1);
  });

  it('should throw an error if required inputs are not set before detectChanges', () => {
    const emptyFixture = TestBed.createComponent(BaseFormComponent);
    expect(() => emptyFixture.detectChanges())
      .toThrow();
  });
});
