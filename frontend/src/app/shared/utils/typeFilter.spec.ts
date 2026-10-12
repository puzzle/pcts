import {
  certificateType1,
  certificateType2,
  degreeType1,
  degreeType2,
  experienceType1,
  experienceType2,
  leadershipExperienceType1,
  leadershipExperienceType2
} from '../test/test-data';
import { filterMultipleFields, filterType } from './typeFilter';

describe('TypeFilter', () => {
  const testCases = [
    {
      name: 'Degrees',
      options: [degreeType1,
        degreeType2],
      value: 'Bac',
      expected: [degreeType1]
    },
    {
      name: 'Certificates',
      options: [certificateType1,
        certificateType2],
      value: 'Git',
      expected: [certificateType1]
    },
    {
      name: 'Leadership',
      options: [leadershipExperienceType1,
        leadershipExperienceType2],
      value: 'Exp',
      expected: [leadershipExperienceType1]
    },
    {
      name: 'Experience',
      options: [experienceType1,
        experienceType2],
      value: 'Int',
      expected: [experienceType1]
    }
  ];

  describe.each(testCases)('Filtering $name', ({ options, value, expected }) => {
    it(`should filter correctly for search value: "${value}"`, () => {
      const result = filterType(value, options, 'name');
      expect(result)
        .toEqual(expected);
    });

    it('should return all options when search value is empty', () => {
      const result = filterType('', options, 'name');
      expect(result)
        .toEqual(options);
    });
  });

  describe('filterMultipleFields', () => {
    interface SampleData {
      id: number;
      title: string;
      publisher: string;
      points: number | null;
      active?: boolean;
    }

    const item: SampleData = {
      id: 1,
      title: 'Angular Developer Master',
      publisher: 'Google',
      points: 100,
      active: true
    };

    it('should return true if searchValue is empty or whitespace only', () => {
      expect(filterMultipleFields(item, '', ['title',
        'publisher']))
        .toBe(true);
      expect(filterMultipleFields(item, '   ', ['title',
        'publisher']))
        .toBe(true);
    });

    it('should match search term in a single specified field (case-insensitive)', () => {
      expect(filterMultipleFields(item, 'angular', ['title']))
        .toBe(true);
      expect(filterMultipleFields(item, 'GOOGLE', ['publisher']))
        .toBe(true);
      expect(filterMultipleFields(item, 'React', ['title']))
        .toBe(false);
    });

    it('should match multiple terms across different fields', () => {
      // 'angular' ist in title, 'google' ist in publisher
      expect(filterMultipleFields(item, 'angular google', ['title',
        'publisher']))
        .toBe(true);
    });

    it('should return false if at least one search term is not found in any field', () => {
      expect(filterMultipleFields(item, 'angular microsoft', ['title',
        'publisher']))
        .toBe(false);
    });

    it('should search non-string fields like numbers and booleans correctly', () => {
      expect(filterMultipleFields(item, '100', ['points']))
        .toBe(true);
      expect(filterMultipleFields(item, 'true', ['active']))
        .toBe(true);
    });

    it('should gracefully handle null and undefined fields', () => {
      const itemWithNulls: SampleData = {
        id: 2,
        title: 'RxJS Basics',
        publisher: 'Community',
        points: null,
        active: undefined
      };

      expect(filterMultipleFields(itemWithNulls, 'rxjs', ['title',
        'points',
        'active']))
        .toBe(true);
      expect(filterMultipleFields(itemWithNulls, 'null', ['points']))
        .toBe(false);
    });

    it('should return false if keys list is empty and search term is provided', () => {
      expect(filterMultipleFields(item, 'angular', []))
        .toBe(false);
    });
  });
});
