import { FormControl } from '@angular/forms';
import { isDateInFuture, isValueInList, isValueInListSignal } from './form-validators';
import { signal } from '@angular/core';
import { add, sub } from 'date-fns';

describe('isDateInFuture', () => {
  it('should return null if value is empty', () => {
    const control = new FormControl('');
    expect(isDateInFuture()(control))
      .toBeNull();
  });

  it('should return invalid_date if date is not valid', () => {
    const control = new FormControl('not-a-date');
    expect(isDateInFuture()(control))
      .toEqual({ invalid_date: true });
  });

  it('should return date_is_in_future if date is in the future', () => {
    const futureDate = add(Date.now(), { days: 1 });
    const control = new FormControl(futureDate);
    expect(isDateInFuture()(control))
      .toEqual({ date_is_in_future: true });
  });

  it('should return null if date is in the past', () => {
    const pastDate = sub(Date.now(), { days: 1 });
    const control = new FormControl(pastDate);
    expect(isDateInFuture()(control))
      .toBeNull();
  });
});

describe('isValueInList', () => {
  const options = ['Apple',
    'Banana',
    'Cherry'];

  it('should return null if value is empty', () => {
    const control = new FormControl('');
    expect(isValueInList(options)(control))
      .toBeNull();
  });

  it('should return null if value is allowed', () => {
    const control = new FormControl('Banana');
    expect(isValueInList(options)(control))
      .toBeNull();
  });

  it('should return invalid_entry if value is not allowed', () => {
    const control = new FormControl('Orange');
    expect(isValueInList(options)(control))
      .toEqual({ invalid_entry: true });
  });
});


describe('isValueInListSignal', () => {
  const optionsSignal = signal(['Red',
    'Green',
    'Blue']);

  it('should return null if value is empty', () => {
    const control = new FormControl('');
    expect(isValueInListSignal(optionsSignal)(control))
      .toBeNull();
  });

  it('should return null if value is in the signal list', () => {
    const control = new FormControl('Green');
    expect(isValueInListSignal(optionsSignal)(control))
      .toBeNull();
  });

  it('should return invalid_entry if value is not in the signal list', () => {
    const control = new FormControl('Yellow');
    expect(isValueInListSignal(optionsSignal)(control))
      .toEqual({ invalid_entry: true });
  });

  it('should react to signal value list changes', () => {
    const control = new FormControl('Purple');

    expect(isValueInListSignal(optionsSignal)(control))
      .toEqual({ invalid_entry: true });

    optionsSignal.set(['Purple']);
    expect(isValueInListSignal(optionsSignal)(control))
      .toBeNull();
  });
});
