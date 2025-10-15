import {
  AfterContentChecked,
  Component,
  computed,
  inject,
  QueryList,
  ViewChildren
} from '@angular/core';
import { MAT_ERROR, MAT_FORM_FIELD, MatError, MatFormField } from '@angular/material/form-field';
import { TranslatePipe } from '@ngx-translate/core';
import { FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'app-form-field-error',
  imports: [MatError,
    TranslatePipe],
  templateUrl: './form-field-error.html',
  styleUrl: './form-field-error.scss',
  providers: [{ provide: MAT_ERROR,
    useValue: FormFieldError }],
  viewProviders: [{ provide: MAT_FORM_FIELD,
    useExisting: MatFormField }]
})
export class FormFieldError implements AfterContentChecked {
  public matFormField = inject(MatFormField);

  private parentF = inject(FormGroupDirective);

  @ViewChildren(MatError) matErrors: QueryList<MatError> | undefined;

  ngAfterContentChecked(): void {
    // this.parentF.(this.matFormField.)
    if (this.matErrors) {
      this.matFormField._errorChildren = this.matErrors;

      console.log(this.matErrors);
      console.log(this.matFormField);
    }
  }

  errors = computed(() => {});

  // public errorKeys = computed(() => this.errors() ? Object.keys(this.errors()!) : []);

  getErrorMessages(): string[] {
    const control = this.matFormField._control.ngControl;
    if (!control || !control.touched || control.valid) {
      return [];
    }
    console.log(control.errors);

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, Number.MAX_SAFE_INTEGER) : [];
  }
}
