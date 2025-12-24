import { TestBed } from '@angular/core/testing';

import { provideTranslateService } from '@ngx-translate/core';
import { ScopedTranslationCoreService } from './scoped-translation-core.service';


describe('ScopedTranslationService', () => {
  let service: ScopedTranslationCoreService;


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideTranslateService(),
        ScopedTranslationCoreService]
    });
    service = TestBed.inject(ScopedTranslationCoreService);
  });

  it('should be created', () => {
    service = TestBed.inject(ScopedTranslationCoreService);
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

    const stringVariationsSpy = jest.spyOn(service as any, 'getStringVariations')
      .mockReturnValue([variationMock]);
    const getKeyListSpy = jest.spyOn(service as any, 'getKeyList')
      .mockReturnValue(keyListMock);
    const getTranslationSpy = jest.spyOn(service as any, 'getTranslation')
      .mockReturnValue(translationMock);

    service.instant('TEST.KEY', {}, prefix);

    expect(stringVariationsSpy)
      .toHaveBeenCalledWith(prefix, key);
    expect(getKeyListSpy)
      .toHaveBeenCalledWith(prefix, key);
    expect(getTranslationSpy)
      .toHaveBeenCalledWith(keyListMock, {});
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
      const result = service['getKeyList'](prefix, suffix);
      expect(result)
        .toEqual(expected);
    });
  });
});
