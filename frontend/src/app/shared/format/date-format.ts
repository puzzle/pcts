import { MatDateFormats } from '@angular/material/core';

export const GLOBAL_DATE_FORMAT = 'dd.MM.yyyy';

export const CUSTOM_LUXON_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: GLOBAL_DATE_FORMAT
  },
  display: {
    dateInput: GLOBAL_DATE_FORMAT,

    monthYearLabel: 'MMMM yyyy',

    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy'
  }
};
