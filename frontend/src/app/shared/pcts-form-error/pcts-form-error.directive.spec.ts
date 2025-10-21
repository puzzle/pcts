import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PctsFormErrorDirective } from './pcts-form-error.directive';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { PctsFormLabelDirective } from '../pcts-form-label/pcts-form-label.directive';

@Component({
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    PctsFormErrorDirective,
    PctsFormLabelDirective
  ],
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
  let translateService: TranslateService;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [TestComponent,
        TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    translateService = TestBed.inject(TranslateService);

    jest.spyOn(translateService, 'instant')
      .mockImplementation((key) => `translated-${key}`);

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
    expect(element.textContent)
      .toContain('translated-VALIDATION.REQUIRED');
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
