import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PctsFormErrorDirective } from './pcts-form-error.directive';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { PctsFormLabelDirective } from '../pcts-form-label/pcts-form-label.directive';
import { translationMock } from '../../../../setup-jest';
import { ScopedTranslationService } from '../services/scoped-translation.service';

@Component({
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    PctsFormErrorDirective,
    PctsFormLabelDirective
  ],
  providers: [{ provide: ScopedTranslationService,
    useValue: translationMock }],
  template: `
    <mat-form-field>
      <mat-label appPctsFormLabel>Label</mat-label>
      <input matInput [formControl]="testControl">
      <mat-error appPctsFormError />
    </mat-form-field>`
})
class TestComponent {
  testControl = new FormControl('', [Validators.required,
    Validators.minLength(5)]);
}

describe('PctsFormErrorDirective', () => {
  let fixture: ComponentFixture<TestComponent>;
  let component: TestComponent;


  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [TestComponent],
      providers: []
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create error directive', () => {
    component.testControl.setErrors({ required: true });
    component.testControl.markAsTouched();
    fixture.detectChanges();

    const debugEl = fixture.debugElement.query(By.directive(PctsFormErrorDirective));
    expect(debugEl)
      .toBeTruthy();

    const directiveInstance = debugEl.injector.get(PctsFormErrorDirective);
    expect(directiveInstance)
      .toBeTruthy();
  });

  it('should update error messages when control is invalid', () => {
    component.testControl.setErrors({ required: true });
    component.testControl.markAsTouched();

    fixture.detectChanges();

    const debugEl = fixture.debugElement.query(By.directive(PctsFormErrorDirective));
    const directiveInstance = debugEl.injector.get(PctsFormErrorDirective);
    directiveInstance.updateError();

    const element = debugEl.nativeElement;
    expect(translationMock.instant)
      .toHaveBeenCalledWith('VALIDATION.REQUIRED');
    expect(element.textContent)
      .toContain('VALIDATION.REQUIRED');
  });

  it('should return error keys in proper format', () => {
    component.testControl.setErrors({ minlength: true,
      required: true });
    component.testControl.markAsTouched();
    fixture.detectChanges();

    const debugEl = fixture.debugElement.query(By.directive(PctsFormErrorDirective));
    expect(debugEl)
      .toBeTruthy();

    const directiveInstance = debugEl.injector.get(PctsFormErrorDirective);
    const errors = directiveInstance.getErrorMessages();

    expect(errors)
      .toEqual(['VALIDATION.MINLENGTH',
        'VALIDATION.REQUIRED']);
  });
});
