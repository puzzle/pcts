import { TranslationKeyPath } from './translation-key-path';

describe('TranslationKeyPath', () => {
  const candidates = (prefix: string, key: string): readonly string[] => TranslationKeyPath.fromPrefix(prefix)
    .candidatesFor(key);

  describe('candidatesFor', () => {
    it.each([
      [
        'no prefix',
        '',
        'X',
        ['X']
      ],
      [
        'depth 1',
        'A',
        'X',
        ['A.X',
          'X']
      ],
      [
        'depth 2',
        'A.B',
        'X',
        [
          'A.B.X',
          'B.X',
          'A.X',
          'X'
        ]
      ],
      [
        'depth 3',
        'A.B.C',
        'X',
        [
          'A.B.C.X',
          'B.C.X',
          'A.B.X',
          'C.X',
          'A.X',
          'X'
        ]
      ],
      [
        'depth 4',
        'A.B.C.D',
        'X',
        [
          'A.B.C.D.X',
          'B.C.D.X',
          'A.B.C.X',
          'C.D.X',
          'A.B.X',
          'D.X',
          'A.X',
          'X'
        ]
      ],
      [
        'dotted key stays intact',
        'A.B',
        'C.D',
        [
          'A.B.C.D',
          'B.C.D',
          'A.C.D',
          'C.D'
        ]
      ]
    ])('%s', (
      _desc, prefix, key, expected
    ) => {
      expect(candidates(prefix, key))
        .toEqual(expected);
    });

    it.each([
      ['double separator',
        'A..B'],
      ['trailing separator',
        'A.B.'],
      ['leading separator',
        '.A.B'],
      ['both ends',
        '.A..B.']
    ])('normalizes %s to A.B', (_desc, prefix) => {
      expect(candidates(prefix, 'X'))
        .toEqual([
          'A.B.X',
          'B.X',
          'A.X',
          'X'
        ]);
    });

    it.each([['empty string',
      ''],
    ['separator only',
      '.'],
    ['separators only',
      '...']])('treats %s as the root scope', (_desc, prefix) => {
      expect(candidates(prefix, 'X'))
        .toEqual(['X']);
    });

    it('starts with the fully qualified key', () => {
      for (const prefix of [
        '',
        'A',
        'A.B',
        'A.B.C'
      ]) {
        const path = TranslationKeyPath.fromPrefix(prefix);
        expect(path.candidatesFor('X')[0])
          .toBe(path.fullyQualified('X'));
      }
    });

    it('ends with the bare key', () => {
      for (const prefix of [
        '',
        'A',
        'A.B',
        'A.B.C',
        'A.B.C.D'
      ]) {
        const result = candidates(prefix, 'X');
        expect(result[result.length - 1])
          .toBe('X');
      }
    });

    it('never repeats a candidate', () => {
      for (const prefix of [
        '',
        'A',
        'A.B',
        'A.B.C',
        'A.B.C.D',
        'A.A.A'
      ]) {
        const result = candidates(prefix, 'X');
        expect(new Set(result).size)
          .toBe(result.length);
      }
    });

    it('is ordered by descending scope length', () => {
      const lengths = candidates('A.B.C.D', 'X')
        .map((c) => c.split('.').length);
      expect(lengths)
        .toEqual([...lengths].sort((a, b) => b - a));
    });

    it('grows linearly: 2n candidates at depth n', () => {
      // Guards against a regression to the old n²/2 cross product.
      expect(candidates('A.B.C.D.E.F', 'X'))
        .toHaveLength(12);
    });

    it('never invents a segment', () => {
      const scope = ['A',
        'B',
        'C'];
      for (const candidate of candidates(scope.join('.'), 'X')) {
        expect(scope)
          .toEqual(expect.arrayContaining(candidate.split('.')
            .slice(0, -1)));
      }
    });

    it('returns a frozen array', () => {
      expect(Object.isFrozen(candidates('A.B', 'X')))
        .toBe(true);
    });
  });

  describe('interior scopes are unreachable', () => {
    /*
     * Deliberate limitation, not an oversight. `B` sits between a leading and
     * a trailing run of A.B.C, so no hoist and no generalization isolates it.
     * A shared block must live at the JSON root or on the prefix's leading
     * path — this is why FORM.BUTTONS.CANCEL cannot serve MEMBER.FORM.ADD.
     */
    it('omits the interior single segment', () => {
      expect(candidates('A.B.C', 'X')).not.toContain('B.X');
    });

    it('omits gapped scopes', () => {
      expect(candidates('A.B.C', 'X')).not.toContain('A.C.X');
    });
  });

  describe('fullyQualified', () => {
    it.each([
      ['A.B.C',
        'X',
        'A.B.C.X'],
      ['A',
        'X',
        'A.X'],
      ['',
        'X',
        'X'],
      ['A..B',
        'X',
        'A.B.X'],
      ['A.B',
        'C.D',
        'A.B.C.D']
    ])('prefix "%s" + key "%s"', (prefix, key, expected) => {
      expect(TranslationKeyPath.fromPrefix(prefix)
        .fullyQualified(key))
        .toBe(expected);
    });
  });
});
