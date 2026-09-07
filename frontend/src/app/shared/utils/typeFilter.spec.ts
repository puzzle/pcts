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
import { filterType } from './typeFilter';

describe('TypeFilter', () => {
  const testCases = [
    {
      name: 'Degrees',
      options: [degreeType1, degreeType2],
      value: "Bac",
      expected: [degreeType1]
    },
    {
      name: 'Certificates',
      options: [certificateType1, certificateType2],
      value: "Git",
      expected: [certificateType1]
    },
    {
      name: 'Leadership',
      options: [leadershipExperienceType1, leadershipExperienceType2],
      value: "Exp",
      expected: [leadershipExperienceType1]
    },
    {
      name: 'Experience',
      options: [experienceType1, experienceType2],
      value: "Int",
      expected: [experienceType1]
    },
  ];

  describe.each(testCases)('Filtering $name', ({ options, value, expected }) => {

    it(`should filter correctly for search value: "${value}"`, () => {
      const result = filterType(value, options, "name");
      expect(result).toEqual(expected);
    });

    it('should return all options when search value is empty', () => {
      const result = filterType("", options, "name");
      expect(result).toEqual(options);
    });

  });
});
