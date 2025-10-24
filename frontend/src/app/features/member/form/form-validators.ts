import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { Signal } from '@angular/core';

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

export function isInstanceOfClassSignal<T>(validOptionsSignal: Signal<T[]>): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: any = control.value;
    const validOptions: T[] = validOptionsSignal();

    if (!value) {
      return null;
    }

    if (typeof value === 'string') {
      return { invalid_entry: true };
    }

    const isValidOption: boolean = validOptions.includes(value);

    return isValidOption ? null : { invalid_entry: true };
  };
}


export function isAEmploymentState(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    if (Object.values(EmploymentState)
      .includes(value)) {
      return null;
    }
    return { invalid_entry: true };
  };
}
