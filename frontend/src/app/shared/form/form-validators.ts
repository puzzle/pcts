import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
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

export function isValueInList<T>(validOptions: T[]): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: any = control.value;

    if (!value) {
      return null;
    }

    const isValidOption: boolean = validOptions.includes(value);

    return isValidOption ? null : { invalid_entry: true };
  };
}

export function isValueInListSignal<T>(validOptionsSignal: Signal<T[]>): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: any = control.value;
    const validOptions: T[] = validOptionsSignal();

    if (!value) {
      return null;
    }

    const isValidOption: boolean = validOptions.includes(value);

    return isValidOption ? null : { invalid_entry: true };
  };
}
