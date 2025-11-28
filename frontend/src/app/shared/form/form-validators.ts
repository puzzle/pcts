import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Signal } from '@angular/core';
import { isPast, isValid } from 'date-fns';

export function isDateInFuture(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    const date = new Date(value);

    if (!isValid(date)) {
      return { invalid_date: true };
    }

    if (!isPast(date)) {
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
