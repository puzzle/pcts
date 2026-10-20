import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Signal } from '@angular/core';
import { isFuture, isToday, isValid } from 'date-fns';

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

export function isValueInList<T>(validOptions: T[], comparator: (a: T, b: T) => boolean = (a, b) => a === b): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: any = control.value;

    if (!value) {
      return null;
    }

    const isValidOption: boolean = validOptions.some((option) => comparator(option, value));

    return isValidOption ? null : { invalid_entry: true };
  };
}

export function isValueInListSignal<T>(validOptionsSignal: Signal<T[]>, comparator: (a: T, b: T) => boolean = (a, b) => a === b): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: any = control.value;
    const validOptions: T[] = validOptionsSignal();
    if (!value) {
      return null;
    }

    const isValidOption: boolean = validOptions.some((option) => comparator(option, value));

    return isValidOption ? null : { invalid_entry: true };
  };
}

export function isInteger(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (value === '') {
      return null;
    }
    if ((/^-?\d+$/).test(value)) {
      return null;
    }
    return { invalid_integer: true };
  };
}

export function isSearchTermEmpty(inputFieldTagId: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!inputFieldTagId) {
      return null;
    }

    const inputfield = document.getElementById(inputFieldTagId) as HTMLInputElement;

    if (!inputfield) {
      return null;
    }

    return inputfield.value ? { invalid_entry: inputfield.value } : null;
  };
}

export function isBetweenUpperAndLower(lower: number, upper: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (value <= upper && value >= lower) {
      return null;
    }

    return { invalid_integer: true };
  };
}

export function doesAddUpToTarget(target: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const highly = Number(control.get('highlyRelevant')?.value);
    const limited = Number(control.get('limitedRelevant')?.value);
    const little = Number(control.get('littleRelevant')?.value);

    if (highly + limited + little !== target) {
      console.log('invalid', highly + limited + little);
      return { invalid_total: true };
    }

    return null;
  };
}
