import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';
import {EmploymentState} from '../../../shared/enum/employment-state.enum';

export function isDateInFuture(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }

    const date = new Date(control.value);
    const today = new Date();

    if (date >= today) {
      return { date_is_in_future: true };
    }

    return null;
  };
}

export function isInstanceOfClass(validOptions: any[]): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    if (typeof value === 'string') {
      return { invalid_entry: true };
    }

    const isValidOption = validOptions
      .includes(value);

    return isValidOption ? null : { invalid_entry: true };
  };
}

export function isAEmploymentState(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    if (value === EmploymentState.EX_MEMBER || value === EmploymentState.APPLICANT || value === EmploymentState.MEMBER) {
      return null;
    }
    return { invalid_entry: true };
  };
}
