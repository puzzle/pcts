import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BaseFormComponent } from './base-form.component';
import { CaseFormatter } from '../format/case-formatter';
import { provideTranslateService } from '@ngx-translate/core';

describe('BaseFormComponent', () => {
  let component: BaseFormComponent;
  let fixture: ComponentFixture<BaseFormComponent>;
  let caseFormatterMock: any;

  beforeEach(async() => {
    caseFormatterMock = { camelToSnake: jest.fn((value: string) => 'TEST_FORM_NAME') };

    await TestBed.configureTestingModule({
      imports: [BaseFormComponent],
      providers: [{ provide: CaseFormatter,
        useValue: caseFormatterMock },
      provideTranslateService()]
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

    expect(component)
      .toBeTruthy();
  });

  it('should compute i18nPrefix correctly from formName', () => {
    fixture.detectChanges();

    expect(component.i18nPrefix())
      .toBe('TEST_FORM_NAME.FORM');
    expect(caseFormatterMock.camelToSnake)
      .toHaveBeenCalledWith('testForm');
  });

  it('should update i18nPrefix when formName input changes', () => {
    fixture.detectChanges();

    expect(component.i18nPrefix())
      .toBe('TEST_FORM_NAME.FORM');
    expect(caseFormatterMock.camelToSnake)
      .toHaveBeenCalledWith('testForm');
  });

  it('should have default isEdit value of false', () => {
    fixture.detectChanges();

    expect(component.isEdit())
      .toBe(false);
  });

  it('should accept a true value for isEdit input', () => {
    fixture.componentRef.setInput('isEdit', true);
    fixture.detectChanges();

    expect(component.isEdit())
      .toBe(true);
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
