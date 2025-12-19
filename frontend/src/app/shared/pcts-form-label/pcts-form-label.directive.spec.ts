import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PctsFormLabelDirective } from './pcts-form-label.directive';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CaseFormatter } from '../format/case-formatter';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { PctsFormErrorDirective } from '../pcts-form-error/pcts-form-error.directive';
import { ScopedTranslationService } from '../services/scoped-translation-core.service';
import { translationMock } from '../../../../setup-jest';

@Component({
  standalone: true,
  imports: [
    MatFormField,
    MatLabel,
    MatError,
    MatInputModule,
    PctsFormLabelDirective,
    PctsFormErrorDirective,
    ReactiveFormsModule
  ],
  providers: [{ provide: ScopedTranslationService,
    useValue: translationMock }],
  template: `
    <form name="testForm">
      <mat-form-field>
        <mat-label appPctsFormLabel>Label</mat-label>
        <input matInput [formControl]="testControl" name="testControl">
        <mat-error appPctsFormError />
      </mat-form-field>
    </form>`
})
class TestComponent {
  testControl = new FormControl('', [Validators.required,
    Validators.minLength(5)]);
}

describe('PctsFormLabelDirective', () => {
  let fixture: ComponentFixture<TestComponent>;


  beforeEach(async() => {
    const caseFormatterMock = { camelToSnake: jest.fn((key) => `formatted_${key}`) };

    await TestBed.configureTestingModule({
      imports: [TestComponent],
      providers: [{ provide: CaseFormatter,
        useValue: caseFormatterMock }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    fixture.detectChanges();
  });

  it('should create label directive', () => {
    const debugEl = fixture.debugElement.query(By.directive(PctsFormLabelDirective));
    expect(debugEl)
      .toBeTruthy();

    const directiveInstance = debugEl.injector.get(PctsFormLabelDirective);
    expect(directiveInstance)
      .toBeTruthy();
  });

  it('should translate and set label text correctly', (done) => {
    const debugEl = fixture.debugElement.query(By.directive(PctsFormLabelDirective));
    const directive = debugEl.injector.get(PctsFormLabelDirective);
    const labelElement = debugEl.nativeElement as HTMLLabelElement;

    jest.spyOn(directive, 'matFormFieldControl', 'get')
      .mockReturnValue({ name: 'testControl' } as any);

    directive.ngAfterViewInit();
    fixture.detectChanges();

    expect(labelElement.innerHTML)
      .toBe('formatted_testControl');
    done();
  });
});
