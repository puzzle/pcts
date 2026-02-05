import { MatDateFormats } from '@angular/material/core';
import { format } from 'date-fns';
import { de } from 'date-fns/locale';

export const GLOBAL_DATE_FORMAT = 'dd.MM.yyyy';

export const GLOBAL_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: { month: 'short',
      year: 'numeric',
      day: 'numeric' }
  },
  display: {
    dateInput: { year: 'numeric',
      month: '2-digit',
      day: '2-digit' },
    monthYearLabel: { year: 'numeric',
      month: 'short' },
    dateA11yLabel: { year: 'numeric',
      month: 'long',
      day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric',
      month: 'long' }
  }
};

/**
 * This functions checks if a string is formatted like YYYY-MM-DD.
 * Returns true if it is
 *
 * @param text the string to check
 */
export function isSimpleISODate(text: string): boolean {
  if (!text) {
    return false;
  }

  return text.matchAll(/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/g) !== null;
}

export function formatDateLocale(date: Date): string {
  return format(date, 'P', { locale: de });
}
