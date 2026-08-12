import { TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';

import { ScopedTranslationCoreService } from './scoped-translation-core.service';
import { TranslationKeyPath } from './translation-key-path';

jest.mock('./translation-key-path');

const { fromPrefix } = jest.mocked(TranslationKeyPath);
const { candidatesFor, fullyQualified } = jest.mocked(TranslationKeyPath.prototype);
const translationKeyPathMock = { fromPrefix,
  candidatesFor,
  fullyQualified };

/** Deliberately unrelated to PREFIX/KEY so no assertion can pass by identity. */
const PREFIX = 'PREFIX.SCOPE';
const KEY = 'THE_KEY';
const FIRST = 'ALPHA';
const SECOND = 'BRAVO';
const THIRD = 'CHARLIE';
const CANDIDATES: readonly string[] = [FIRST,
  SECOND,
  THIRD];
const FALLBACK = 'ZULU';

describe('ScopedTranslationCoreService', () => {
  let service: ScopedTranslationCoreService;


  const translateServiceMock = {
    instant: jest.fn()
  };

  const fromPrefixMock = TranslationKeyPath.fromPrefix as unknown as jest.Mock;

  /** Every candidate misses: ngx-translate echoes the key back. */
  const missAll = (): void => {
    translateServiceMock.instant.mockImplementation((candidate: string) => candidate);
  };

  /** Only `winner` resolves, to `value`. */
  const resolveOnly = (winner: string, value: unknown): void => {
    translateServiceMock.instant.mockImplementation((candidate: string) => (candidate === winner ? value : candidate));
  };

  beforeEach(() => {
    jest.resetAllMocks();

    translationKeyPathMock.candidatesFor.mockReturnValue(CANDIDATES);
    translationKeyPathMock.fullyQualified.mockReturnValue(FALLBACK);
    translationKeyPathMock.fromPrefix.mockReturnValue(TranslationKeyPath.prototype);

    TestBed.configureTestingModule({
      providers: [ScopedTranslationCoreService,
        { provide: TranslateService,
          useValue: translateServiceMock }]
    });

    service = TestBed.inject(ScopedTranslationCoreService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('delegation to the key path', () => {
    beforeEach(() => resolveOnly(FIRST, 'value'));

    it('builds the path from the given prefix', () => {
      service.instant(KEY, undefined, PREFIX);

      expect(fromPrefixMock)
        .toHaveBeenCalledWith(PREFIX);
    });

    it('defaults the prefix to the root scope', () => {
      service.instant(KEY);

      expect(fromPrefixMock)
        .toHaveBeenCalledWith('');
    });

    it('asks the path for candidates of the requested key', () => {
      service.instant(KEY, undefined, PREFIX);

      expect(translationKeyPathMock.candidatesFor)
        .toHaveBeenCalledWith(KEY);
    });

    it('does not ask for the fallback on a hit', () => {
      service.instant(KEY, undefined, PREFIX);

      expect(translationKeyPathMock.fullyQualified).not.toHaveBeenCalled();
    });
  });

  describe('candidate probing', () => {
    it('probes candidates in the order the path returned them', () => {
      resolveOnly(THIRD, 'value');

      service.instant(KEY, undefined, PREFIX);

      expect(translateServiceMock.instant.mock.calls)
        .toEqual([[FIRST],
          [SECOND],
          [THIRD]]);
    });

    it('probes without params, so a miss never interpolates', () => {
      resolveOnly(THIRD, 'value');

      service.instant(KEY, { name: 'Minder' }, PREFIX);

      const probes = translateServiceMock.instant.mock.calls.slice(0, 3);
      expect(probes.every((call) => call.length === 1))
        .toBe(true);
    });

    it('stops at the first hit', () => {
      resolveOnly(SECOND, 'value');

      service.instant(KEY, undefined, PREFIX);

      expect(translateServiceMock.instant).not.toHaveBeenCalledWith(THIRD);
    });

    it('returns the resolved translation', () => {
      resolveOnly(SECOND, 'Abbrechen');

      expect(service.instant(KEY, undefined, PREFIX))
        .toBe('Abbrechen');
    });
  });

  describe('miss detection', () => {
    it('treats an echoed key as a miss and moves on', () => {
      resolveOnly(SECOND, 'value');

      expect(service.instant(KEY, undefined, PREFIX))
        .toBe('value');
      expect(translateServiceMock.instant)
        .toHaveBeenCalledWith(SECOND);
    });

    it.each([
      ['a subtree object',
        { ACTION: 'löschen' }],
      ['an array',
        ['a',
          'b']],
      ['null',
        null],
      ['a number',
        42]
    ])('skips a candidate resolving to %s', (_desc, nonString) => {
      translateServiceMock.instant.mockImplementation((candidate: string) => {
        if (candidate === FIRST) {
          return nonString;
        }

        return candidate === SECOND ? 'value' : candidate;
      });

      expect(service.instant(KEY, undefined, PREFIX))
        .toBe('value');
    });
  });

  describe('interpolation', () => {
    it('re-reads only the winner with params', () => {
      const params = { name: 'Minder' };
      translateServiceMock.instant.mockImplementation((candidate: string, given?: unknown) => {
        if (candidate !== SECOND) {
          return candidate;
        }

        return given === undefined ? 'Hallo {{name}}' : 'Hallo Minder';
      });

      expect(service.instant(KEY, params, PREFIX))
        .toBe('Hallo Minder');
      expect(translateServiceMock.instant.mock.calls)
        .toEqual([[FIRST],
          [SECOND],
          [SECOND,
            params]]);
    });

    it('returns the probe directly when no params are given', () => {
      resolveOnly(FIRST, 'Wert');

      expect(service.instant(KEY, undefined, PREFIX))
        .toBe('Wert');
      expect(translateServiceMock.instant)
        .toHaveBeenCalledTimes(1);
    });

    it('re-reads on empty params, since undefined is the only opt-out', () => {
      const params = {};
      resolveOnly(FIRST, 'Wert');

      service.instant(KEY, params, PREFIX);

      expect(translateServiceMock.instant.mock.calls)
        .toEqual([[FIRST],
          [FIRST,
            params]]);
    });
  });

  describe('total miss', () => {
    let warn: jest.SpyInstance;

    beforeEach(() => {
      warn = jest.spyOn(console, 'warn')
        .mockImplementation(() => undefined);
      missAll();
    });

    afterEach(() => warn.mockRestore());

    it('returns the fully qualified key', () => {
      expect(service.instant(KEY, undefined, PREFIX))
        .toBe(FALLBACK);
      expect(translationKeyPathMock.fullyQualified)
        .toHaveBeenCalledWith(KEY);
    });

    it('warns once, naming the key and every candidate tried', () => {
      service.instant(KEY, undefined, PREFIX);

      expect(warn)
        .toHaveBeenCalledTimes(1);

      const [message,
        logged] = warn.mock.calls[0];
      expect(message)
        .toContain(FALLBACK);
      expect(logged)
        .toEqual(CANDIDATES);
    });

    it('does not probe again while reporting', () => {
      service.instant(KEY, undefined, PREFIX);

      expect(translateServiceMock.instant)
        .toHaveBeenCalledTimes(CANDIDATES.length);
    });

    it('stays silent on a hit', () => {
      resolveOnly(FIRST, 'value');

      service.instant(KEY, undefined, PREFIX);

      expect(warn).not.toHaveBeenCalled();
    });
  });
});
