import { setupZonelessTestEnv } from 'jest-preset-angular/setup-env/zoneless';
import {TestBed} from "@angular/core/testing";
import {ScopedTranslationService} from "./src/app/shared/services/scoped-translation.service";
import {I18N_PREFIX} from "./src/app/shared/i18n-prefix.token";
import {DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter } from '@angular/material/core';
import {I18N_PREFIX} from "./src/app/shared/i18n-prefix.token";
import { GLOBAL_DATE_FORMATS } from './src/app/shared/format/date-format'
import {ScopedTranslationService} from "./src/app/shared/i18n-prefix.provider";
import {TestBed} from "@angular/core/testing";

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
            useClass: NativeDateAdapter },
          { provide: MAT_DATE_FORMATS,
            useValue: GLOBAL_DATE_FORMATS },
        ]
    });
});


beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [],
        providers: [
            {provide: ScopedTranslationService, useValue: translationMock},
            {provide: I18N_PREFIX, useValue: "GLOBAL.DEFAULT.PREFIX"}
        ]
    });
});