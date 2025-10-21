import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PctsFormLabelDirective } from './pcts-form-label.directive';
import { MatFormField, MatLabel, MatError } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { CaseFormatter } from '../format/case-formatter';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { PctsFormErrorDirective } from '../pcts-form-error/pcts-form-error.directive';
import { of } from 'rxjs';

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
  let translateService: TranslateService;

  beforeEach(async() => {
    const caseFormatterMock = { camelToSnake: jest.fn((key) => `formatted_${key}`) };

    await TestBed.configureTestingModule({
      imports: [TestComponent,
        TranslateModule.forRoot()],
      providers: [{ provide: CaseFormatter,
        useValue: caseFormatterMock }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    translateService = TestBed.inject(TranslateService);
    caseFormatter = TestBed.inject(CaseFormatter);

    jest.spyOn(translateService, 'get')
      .mockImplementation((key) => of(`translated-${key}`));

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

  it('should set i18nPrefix based on form name', () => {
    const directive = fixture.debugElement.query(By.directive(PctsFormLabelDirective)).injector.get(PctsFormLabelDirective);
    expect(directive.i18nPrefix)
      .toBe('testForm');
  });

  // it('should translate and set label text correctly', (done) => {

  // });
});
