import { MatDateFormats } from '@angular/material/core';

export const DATE_FORMAT: MatDateFormats = {
  parse: {
    dateInput: 'DD.MM.yyyy'
  },
  display: {
    dateInput: 'DD.MM.yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy'
  }
};
