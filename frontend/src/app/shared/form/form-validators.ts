import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Signal } from '@angular/core';
import { isFuture, isPast, isToday, isValid } from 'date-fns';

export function isDateInPastOrPresent(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    const date = new Date(value);

    if (!isValid(date)) {
      return { invalid_date: true };
    }

    if (isFuture(date)) {
      return { date_is_in_future: true };
    }

    return null;
  };
}

export function isDateInPast(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    const date = new Date(value);

    if (!isValid(date)) {
      return { invalid_date: true };
    }

    if (isFuture(date) || isToday(date)) {
      return { date_is_not_in_past: true };
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
