import { setupZonelessTestEnv } from 'jest-preset-angular/setup-env/zoneless';
import {DateAdapter, MAT_DATE_FORMATS} from '@angular/material/core';
import {LuxonDateAdapter} from '@angular/material-luxon-adapter';
import {CUSTOM_LUXON_DATE_FORMATS} from './src/app/shared/format/date-format';
import {TestBed} from "@angular/core/testing";
import {ScopedTranslationService} from "./src/app/shared/services/scoped-translation.service";
import {I18N_PREFIX} from "./src/app/shared/i18n-prefix.token";

setupZonelessTestEnv();
export const translationMock : Partial<jest.Mocked<ScopedTranslationService>> ={
    instant: jest.fn().mockImplementation((key:string)=> key)
}

beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [],
        providers: [
            {provide: ScopedTranslationService, useValue: translationMock},
            {provide: I18N_PREFIX, useValue: "GLOBAL.DEFAULT.PREFIX"},
          { provide: DateAdapter,
            useClass: LuxonDateAdapter },
          { provide: MAT_DATE_FORMATS,
            useValue: CUSTOM_LUXON_DATE_FORMATS },
        ]
    });
});
