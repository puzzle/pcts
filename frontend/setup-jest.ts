import { setupZonelessTestEnv } from 'jest-preset-angular/setup-env/zoneless';
import {DateAdapter, MAT_DATE_FORMATS} from '@angular/material/core';
import {LuxonDateAdapter} from '@angular/material-luxon-adapter';
import {CUSTOM_LUXON_DATE_FORMATS} from './src/app/shared/format/date-format';
import {TestBed} from '@angular/core/testing';

setupZonelessTestEnv();

beforeEach(() => {
  TestBed.configureTestingModule({
    imports: [],
    providers: [
      { provide: DateAdapter,
        useClass: LuxonDateAdapter },
      { provide: MAT_DATE_FORMATS,
        useValue: CUSTOM_LUXON_DATE_FORMATS },
    ]
  });
});
