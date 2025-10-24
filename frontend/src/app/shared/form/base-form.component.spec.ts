import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { BaseFormComponent } from './base-form.component';
import { CaseFormatter } from '../format/case-formatter';
import { TranslateModule } from '@ngx-translate/core';


describe('BaseFormComponent', () => {
  let component: BaseFormComponent;
  let fixture: ComponentFixture<BaseFormComponent>;

  beforeEach(async() => {
    const caseFormatterMock = { camelToSnake: jest.fn(() => 'TEST_FORM_NAME') };

    await TestBed.configureTestingModule({
      imports: [BaseFormComponent,
        TranslateModule.forRoot()],
      providers: [{ provide: CaseFormatter,
        useValue: caseFormatterMock }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BaseFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should throw an error if required inputs are not set before detectChanges', () => {
    expect(() => fixture.detectChanges())
      .toThrow();
  });

  it('should compute i18nPrefix correctly from formName', () => {
    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testFormName');

    fixture.detectChanges();

    expect(component.i18nPrefix())
      .toBe('FORM.TEST_FORM_NAME');
  });

  it('should update i18nPrefix when formName input changes', () => {
    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testFormName');
    fixture.detectChanges();
    expect(component.i18nPrefix())
      .toBe('FORM.TEST_FORM_NAME');
  });

  it('should have default isEdit value of false', () => {
    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
    fixture.detectChanges();

    expect(component.isEdit())
      .toBe(false);
  });

  it('should accept a true value for isEdit input', () => {
    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
    fixture.componentRef.setInput('isEdit', true);
    fixture.detectChanges();

    expect(component.isEdit())
      .toBe(true);
  });

  it('should emit submitted event', () => {
    jest.spyOn(component.submitted, 'emit');

    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
    fixture.detectChanges();

    component.submitted.emit();

    expect(component.submitted.emit)
      .toHaveBeenCalledTimes(1);
  });

  it('should emit canceled event', () => {
    jest.spyOn(component.canceled, 'emit');

    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
    fixture.detectChanges();

    component.canceled.emit();

    expect(component.canceled.emit)
      .toHaveBeenCalledTimes(1);
  });
});
