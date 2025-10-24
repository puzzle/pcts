import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { MatInput, MatInputModule } from '@angular/material/input';
import { InputFieldComponent } from './input-field.component';
import { PctsFormErrorDirective } from '../pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../pcts-form-label/pcts-form-label.directive';
import { provideTranslateService } from '@ngx-translate/core';

@Component({
  standalone: true,
  imports: [InputFieldComponent,
    ReactiveFormsModule,
    MatInput],
  template: `
    <app-input-field>
      <input matInput [formControl]="control"/>
    </app-input-field>
  `
})
class TestHostComponent {
  control = new FormControl('', Validators.required);
}

describe('InputFieldComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let hostComponent: TestHostComponent;
  let inputFieldDe: DebugElement;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatDatepickerModule,
        MatIconModule,
        MatInputModule,
        InputFieldComponent
      ],
      providers: [
        provideTranslateService(),
        TestHostComponent,
        PctsFormErrorDirective,
        PctsFormLabelDirective
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    hostComponent = fixture.componentInstance;
    fixture.detectChanges();

    inputFieldDe = fixture.debugElement.query(By.directive(InputFieldComponent));
  });

  it('should create the component', () => {
    expect(inputFieldDe)
      .toBeTruthy();
  });

  it('should render mat-form-field', () => {
    const matFormField = inputFieldDe.query(By.css('mat-form-field'));
    expect(matFormField)
      .toBeTruthy();
  });

  it('should render the label with PctsFormLabelDirective', () => {
    const label = inputFieldDe.query(By.directive(PctsFormLabelDirective));
    expect(label)
      .toBeTruthy();
  });

  it('should render mat-error with PctsFormErrorDirective', () => {
    hostComponent.control.setValue('');
    hostComponent.control.markAsTouched();
    hostComponent.control.updateValueAndValidity();
    fixture.detectChanges();

    const error = inputFieldDe.query(By.directive(PctsFormErrorDirective));
    expect(error)
      .toBeTruthy();
  });

  it('should bind form control to mat-form-field control', () => {
    const component = inputFieldDe.componentInstance as InputFieldComponent;

    hostComponent.control.setValue('test value');
    fixture.detectChanges();

    expect(hostComponent.control.value)
      .toBe('test value');
    expect(component.matFormFieldControl())
      .toBeTruthy();
  });
});
