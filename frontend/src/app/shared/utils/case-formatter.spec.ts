import { camelToSnake } from './case-formatter';

describe('CaseFormatter', () => {
  beforeEach(() => {
  });

  it('should convert camelCase to SNAKE_CASE', () => {
    expect(camelToSnake('camelCase'))
      .toBe('CAMEL_CASE');
  });

  it('should handle multiple uppercase letters', () => {
    expect(camelToSnake('thisIsATestString'))
      .toBe('THIS_IS_A_TEST_STRING');
  });

  it('should return uppercase when input has no camel casing', () => {
    expect(camelToSnake('simple'))
      .toBe('SIMPLE');
  });

  it('should handle empty string', () => {
    expect(camelToSnake(''))
      .toBe('');
  });
});
