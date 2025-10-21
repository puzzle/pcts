import { CaseFormatter } from './case-formatter';

describe('CaseFormatter', () => {
  let service: CaseFormatter;

  beforeEach(() => {
    service = new CaseFormatter();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should convert camelCase to SNAKE_CASE', () => {
    expect(service.camelToSnake('camelCase'))
      .toBe('CAMEL_CASE');
  });

  it('should handle multiple uppercase letters', () => {
    expect(service.camelToSnake('thisIsATestString'))
      .toBe('THIS_IS_A_TEST_STRING');
  });

  it('should return uppercase when input has no camel casing', () => {
    expect(service.camelToSnake('simple'))
      .toBe('SIMPLE');
  });

  it('should handle empty string', () => {
    expect(service.camelToSnake(''))
      .toBe('');
  });
});
