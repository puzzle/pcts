import { TestBed } from '@angular/core/testing';

import { ScopedTranslationService } from './scoped-translation.service';
import { I18N_PREFIX } from '../i18n-prefix.token';
import { provideTranslateService } from '@ngx-translate/core';


describe('ScopedTranslationService', () => {
  let service: ScopedTranslationService;
  const i18nPrefixMock = jest.fn()
    .mockReturnValue('DEFAULT.PREFIX');

  function setPrefixMock(prefix: string) {
    i18nPrefixMock.mockReturnValue(prefix);
    service = TestBed.inject(ScopedTranslationService);
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideTranslateService(),
        ScopedTranslationService,
        { provide: I18N_PREFIX,
          useFactory: () => i18nPrefixMock() }]
    });
  });

  it('should be created', () => {
    service = TestBed.inject(ScopedTranslationService);
    expect(service)
      .toBeTruthy();
  });

  it('instant should call proper methods ', () => {
    const prefix = 'THIS.IS.A.TEST.PREFIX';
    const key = 'TEST.KEY';
    const variationMock = { prefix,
      key };
    const keyListMock = ['THIS',
      'TEST'];
    const translationMock = 'Translation';
    setPrefixMock(prefix);

    const stringVariationsSpy = jest.spyOn(service as any, 'getStringVariations')
      .mockReturnValue([variationMock]);
    const getKeyListSpy = jest.spyOn(service as any, 'getKeyList')
      .mockReturnValue(keyListMock);
    const getTranslationSpy = jest.spyOn(service as any, 'getTranslation')
      .mockReturnValue(translationMock);

    service.instant('TEST.KEY');

    expect(stringVariationsSpy)
      .toHaveBeenCalledWith(prefix, key);
    expect(getKeyListSpy)
      .toHaveBeenCalledWith(prefix, key);
    expect(getTranslationSpy)
      .toHaveBeenCalledWith(keyListMock, undefined);
    stringVariationsSpy.mockRestore();
    getKeyListSpy.mockRestore();
    getTranslationSpy.mockRestore();
  });


  describe('getStringVariations', () => {
    const testCases = [{
      prefix: 'A.B.C',
      key: 'D.E',
      expected: [{ prefix: 'A.B.C',
        key: 'D.E' },
      { prefix: 'A.B',
        key: 'C.D.E' },
      { prefix: 'A',
        key: 'B.C.D.E' }],
      desc: '3 variations'
    },
    {
      prefix: 'A',
      key: 'B',
      expected: [{ prefix: 'A',
        key: 'B' }],
      desc: '1 variation'
    },
    {
      prefix: '',
      key: 'B',
      expected: [{ prefix: '',
        key: 'B' }],
      desc: '1 variation'
    }];

    it.each(testCases)('should return $desc for prefix "$prefix" and key "$key"', ({ prefix, key, expected }) => {
      const result = service['getStringVariations'](prefix, key);
      expect(result)
        .toEqual(expected);
    });
  });

  describe('getKeyList', () => {
    const testCases = [{
      prefix: 'A.B.C',
      suffix: 'D.E',
      expected: [
        'A.B.C.D.E',
        'A.B.D.E',
        'A.D.E',
        'D.E'
      ],
      desc: 'A.B.C.D.E, A.B.D.E, A.D.E, D.E'
    },
    {
      prefix: 'A',
      suffix: 'B',
      expected: ['A.B',
        'B'],
      desc: 'A.B, B'
    },
    {
      prefix: '',
      suffix: 'B',
      expected: ['B'],
      desc: 'B'
    }];

    it.each(testCases)('should return [$desc] for prefix "$prefix" and suffix "$suffix"', ({ prefix, suffix, expected }) => {
      const result = (service as any).getKeyList(prefix, suffix);
      expect(result)
        .toEqual(expected);
    });
  });
});
