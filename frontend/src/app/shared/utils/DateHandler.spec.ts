import { removeTimeZone } from './DateHandler';

describe('DateHandler', () => {
  describe('removeTimeZone', () => {
    it('should not set ', () => {

    });

    it('should remove timezone offset by converting to UTC midnight', () => {
      const date = new Date('2024-05-10T15:23:00Z');
      const result = removeTimeZone(date);

      expect(result.getUTCFullYear())
        .toBe(2024);
      expect(result.getUTCMonth())
        .toBe(4);
      expect(result.getUTCDate())
        .toBe(10);
      expect(result.getUTCHours())
        .toBe(0);
      expect(result.getUTCMinutes())
        .toBe(0);
      expect(result.getUTCSeconds())
        .toBe(0);
    });

    it('should correctly handle local timezone offsets', () => {
      const date = new Date(
        2024, 0, 5, 23, 59
      );

      const result = removeTimeZone(date);

      expect(result.getUTCFullYear())
        .toBe(2024);
      expect(result.getUTCMonth())
        .toBe(0);
      expect(result.getUTCDate())
        .toBe(5);
      expect(result.getUTCHours())
        .toBe(0);
    });

    it('should preserve year, month, and day values', () => {
      const date = new Date(
        2030, 11, 31, 12, 45
      );
      const result = removeTimeZone(date);

      expect(result.getUTCFullYear())
        .toBe(2030);
      expect(result.getUTCMonth())
        .toBe(11);
      expect(result.getUTCDate())
        .toBe(31);
    });

    it('should return a new Date instance', () => {
      const input = new Date(2023, 5, 1);
      const result = removeTimeZone(input);

      expect(result).not.toBe(input);
      expect(result)
        .toBeInstanceOf(Date);
    });
  });
});
